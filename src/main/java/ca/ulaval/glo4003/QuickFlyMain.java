package ca.ulaval.glo4003;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.core.Application;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

import ca.ulaval.glo4003.quickflyws.api.account.AccountResource;
import ca.ulaval.glo4003.quickflyws.api.account.AccountResourceApi;
import ca.ulaval.glo4003.quickflyws.api.cart.CartResource;
import ca.ulaval.glo4003.quickflyws.api.cart.CartResourceApi;
import ca.ulaval.glo4003.quickflyws.api.flight.FlightResource;
import ca.ulaval.glo4003.quickflyws.api.flight.FlightResourceApi;
import ca.ulaval.glo4003.quickflyws.api.localization.CityLocator;
import ca.ulaval.glo4003.quickflyws.api.localization.CityLocatorStub;
import ca.ulaval.glo4003.quickflyws.api.payment.PaymentSystemResource;
import ca.ulaval.glo4003.quickflyws.api.payment.PaymentSystemResourceApi;
import ca.ulaval.glo4003.quickflyws.api.weightScale.WeightScale;
import ca.ulaval.glo4003.quickflyws.api.weightScale.WeightScaleStub;
import ca.ulaval.glo4003.quickflyws.domain.account.Account;
import ca.ulaval.glo4003.quickflyws.domain.account.exceptions.AccountAlreadyExistException;
import ca.ulaval.glo4003.quickflyws.domain.cart.cartItem.CartItemFactory;
import ca.ulaval.glo4003.quickflyws.domain.cart.cartItem.planeTicket.PlaneTicketFactory;
import ca.ulaval.glo4003.quickflyws.domain.flight.cargoFlight.CargoFlight;
import ca.ulaval.glo4003.quickflyws.domain.flight.passengerFlight.PassengerFlight;
import ca.ulaval.glo4003.quickflyws.infrastructure.account.AccountDevDataFactory;
import ca.ulaval.glo4003.quickflyws.infrastructure.account.AccountRepositoryInMemory;
import ca.ulaval.glo4003.quickflyws.infrastructure.cart.CartRepositoryInMemory;
import ca.ulaval.glo4003.quickflyws.infrastructure.flight.FlightDevDataFactory;
import ca.ulaval.glo4003.quickflyws.infrastructure.flight.FlightHashGenerator;
import ca.ulaval.glo4003.quickflyws.infrastructure.flight.FlightRepositoryInMemory;
import ca.ulaval.glo4003.quickflyws.infrastructure.flight.flightModificationLogger.FlightModificationLoggerInFile;
import ca.ulaval.glo4003.quickflyws.infrastructure.hashGenerator.FlightHashGeneratorInMemory;
import ca.ulaval.glo4003.quickflyws.infrastructure.paymentSystem.emailService.TransactionDetailsSenderByEmail;
import ca.ulaval.glo4003.quickflyws.infrastructure.paymentSystem.transactionLogger.TransactionLoggerInFile;
import ca.ulaval.glo4003.quickflyws.service.account.AccountAssembler;
import ca.ulaval.glo4003.quickflyws.service.account.AccountRepository;
import ca.ulaval.glo4003.quickflyws.service.account.AccountService;
import ca.ulaval.glo4003.quickflyws.service.cart.CartAssembler;
import ca.ulaval.glo4003.quickflyws.service.cart.CartRepository;
import ca.ulaval.glo4003.quickflyws.service.cart.CartService;
import ca.ulaval.glo4003.quickflyws.service.flight.FlightAssembler;
import ca.ulaval.glo4003.quickflyws.service.flight.FlightModificationLogger;
import ca.ulaval.glo4003.quickflyws.service.flight.FlightPricerFactory;
import ca.ulaval.glo4003.quickflyws.service.flight.FlightRepository;
import ca.ulaval.glo4003.quickflyws.service.flight.FlightService;
import ca.ulaval.glo4003.quickflyws.service.paymentSystem.PaymentSystemService;
import ca.ulaval.glo4003.quickflyws.service.paymentSystem.TransactionAssembler;
import ca.ulaval.glo4003.quickflyws.service.paymentSystem.TransactionDetailsSender;
import ca.ulaval.glo4003.quickflyws.service.paymentSystem.TransactionLogger;
import ca.ulaval.glo4003.ui.CORSResponseFilter;

@SuppressWarnings("all")
public class QuickFlyMain {
  public static boolean isDev = true;

  public static void main(String[] args) throws Exception {
    // Setup resources (API)
    FlightHashGenerator flightHashGenerator = new FlightHashGeneratorInMemory();
    FlightRepository flightRepository = new FlightRepositoryInMemory(flightHashGenerator);
    CartRepository cartRepository = new CartRepositoryInMemory();
    PaymentSystemResource paymentSystemResource = createPaymentSystemResource(cartRepository);
    AccountResource accountResource = createAccountResource();
    FlightResource flightResource = createFlightResource(flightRepository);
    CartResource cartResource = createCartResource(cartRepository, flightRepository);
    WeightScale weightScale = new WeightScaleStub();
    CityLocator cityLocator = new CityLocatorStub();

    // Setup API context (JERSEY + JETTY)
    ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
    context.setContextPath("/api/");
    ResourceConfig resourceConfig = ResourceConfig.forApplication(new Application() {
      @Override
      public Set<Object> getSingletons() {
        HashSet<Object> resources = new HashSet<>();
        // Add resources to context
        resources.add(accountResource);
        resources.add(flightResource);
        resources.add(cartResource);
        resources.add(weightScale);
        resources.add(paymentSystemResource);
        resources.add(cityLocator);
        return resources;
      }
    });
    resourceConfig.register(CORSResponseFilter.class);

    ServletContainer servletContainer = new ServletContainer(resourceConfig);
    ServletHolder servletHolder = new ServletHolder(servletContainer);
    context.addServlet(servletHolder, "/*");

    // Setup static file context (WEBAPP)
    WebAppContext webapp = new WebAppContext();
    webapp.setResourceBase("src/main/webapp");
    webapp.setContextPath("/");

    // Setup http server
    ContextHandlerCollection contexts = new ContextHandlerCollection();
    contexts.setHandlers(new Handler[] { context, webapp });
    Server server = new Server(8080);
    server.setHandler(contexts);

    try {
      server.start();
      server.join();
    } finally {
      server.destroy();
    }
  }

  private static PaymentSystemResource createPaymentSystemResource(CartRepository cartRepository) {
    TransactionAssembler transactionAssembler = new TransactionAssembler();
    CartAssembler cartAssembler = new CartAssembler();

    TransactionLogger transactionLogger = new TransactionLoggerInFile();
    TransactionDetailsSender transactionDetailsSender = new TransactionDetailsSenderByEmail();

    PaymentSystemService paymentSystemService = new PaymentSystemService(transactionLogger, cartRepository, transactionAssembler, cartAssembler,
        transactionDetailsSender);

    PaymentSystemResource paymentSystemResource = new PaymentSystemResourceApi(paymentSystemService);
    return paymentSystemResource;
  }

  private static AccountResource createAccountResource() throws AccountAlreadyExistException {
    AccountRepository accountRepository = new AccountRepositoryInMemory();

    if (isDev) {
      AccountDevDataFactory accountDevDataFactory = new AccountDevDataFactory();
      List<Account> accounts = accountDevDataFactory.createMockData();
      accounts.stream().forEach(account -> {
        try {
          accountRepository.save(account);
        } catch (AccountAlreadyExistException e) {
          e.printStackTrace();
        }
      });
    }

    AccountAssembler accountAssembler = new AccountAssembler();
    AccountService accountService = new AccountService(accountRepository, accountAssembler);

    return new AccountResourceApi(accountService);
  }

  private static FlightResource createFlightResource(FlightRepository flightRepository) {
    if (isDev) {
      FlightDevDataFactory flightDevDataFactory = new FlightDevDataFactory();
      List<PassengerFlight> passengerFlights = flightDevDataFactory.createPassengerFlightData();
      passengerFlights.stream().forEach(flightRepository::savePassengerFlight);

      List<CargoFlight> cargoFlights = flightDevDataFactory.createCargoFlightData();
      cargoFlights.stream().forEach(flightRepository::saveCargoFlight);
    }
    FlightModificationLogger flightModificationLogger = new FlightModificationLoggerInFile();
    FlightAssembler flightAssembler = new FlightAssembler();
    FlightPricerFactory flightPricerFactory = new FlightPricerFactory();
    FlightService flightService = new FlightService(flightRepository, flightAssembler, flightModificationLogger, flightPricerFactory);

    return new FlightResourceApi(flightService);
  }

  private static CartResource createCartResource(CartRepository cartRepository, FlightRepository flightRepository) {
    CartAssembler cartAssembler = new CartAssembler();
    PlaneTicketFactory planeTicketFactory = new PlaneTicketFactory();
    CartItemFactory cartItemFactory = new CartItemFactory(planeTicketFactory);
    FlightPricerFactory flightPricerFactory = new FlightPricerFactory();
    return new CartResourceApi(
        new CartService(cartRepository, flightRepository, cartAssembler, cartItemFactory, planeTicketFactory, flightPricerFactory));
  }

}