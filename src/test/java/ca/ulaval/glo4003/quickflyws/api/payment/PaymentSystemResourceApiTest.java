package ca.ulaval.glo4003.quickflyws.api.payment;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.verify;

import javax.ws.rs.WebApplicationException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ca.ulaval.glo4003.quickflyws.domain.generalExceptions.BadRequestException;
import ca.ulaval.glo4003.quickflyws.domain.generalExceptions.NotFoundException;
import ca.ulaval.glo4003.quickflyws.dto.TransactionDetailsDto;
import ca.ulaval.glo4003.quickflyws.dto.TransactionToProcessDto;
import ca.ulaval.glo4003.quickflyws.infrastructure.email.exceptions.UnsentEmailException;
import ca.ulaval.glo4003.quickflyws.service.paymentSystem.PaymentSystemService;

@RunWith(MockitoJUnitRunner.class)
public class PaymentSystemResourceApiTest {

  @Mock
  private PaymentSystemService paymentSystemService;

  private TransactionDetailsDto transactionDetailsDto;

  private TransactionToProcessDto transactionToProcessDto;

  private PaymentSystemResource paymentSystemResource;

  @Before
  public void setUp() {
    paymentSystemResource = new PaymentSystemResourceApi(paymentSystemService);
  }

  @Test
  public void validTransactionToProcessDto_payingTicketsInCart_delegateToService() {
    paymentSystemResource.payTicketsInCart(transactionToProcessDto);

    verify(paymentSystemService).payTicketsInCart(transactionToProcessDto);
  }

  @Test
  public void validTransactionToProcessDto_payingTicketsInCart_returnTransactionDetailsDto() {
    given(paymentSystemService.payTicketsInCart(transactionToProcessDto)).willReturn(transactionDetailsDto);

    TransactionDetailsDto returnedTransactionDetailsDto = paymentSystemResource.payTicketsInCart(transactionToProcessDto);

    assertEquals(transactionDetailsDto, returnedTransactionDetailsDto);
  }

  @Test(expected = WebApplicationException.class)
  public void unsentEmailException_payingTicketsInCart_throwWebApplication() {
    willThrow(UnsentEmailException.class).given(paymentSystemService).payTicketsInCart(transactionToProcessDto);

    paymentSystemResource.payTicketsInCart(transactionToProcessDto);
  }

  @Test(expected = WebApplicationException.class)
  public void badRequestException_payingTicketsInCart_throwWebApplication() {
    willThrow(BadRequestException.class).given(paymentSystemService).payTicketsInCart(transactionToProcessDto);

    paymentSystemResource.payTicketsInCart(transactionToProcessDto);
  }

  @Test(expected = WebApplicationException.class)
  public void notFoundException_payingTicketsInCart_throwWebApplication() {
    willThrow(NotFoundException.class).given(paymentSystemService).payTicketsInCart(transactionToProcessDto);

    paymentSystemResource.payTicketsInCart(transactionToProcessDto);
  }

}