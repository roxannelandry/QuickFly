package ca.ulaval.glo4003.quickflyws.api.payment;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import ca.ulaval.glo4003.quickflyws.domain.generalExceptions.BadRequestException;
import ca.ulaval.glo4003.quickflyws.domain.generalExceptions.NotFoundException;
import ca.ulaval.glo4003.quickflyws.dto.TransactionDetailsDto;
import ca.ulaval.glo4003.quickflyws.dto.TransactionToProcessDto;
import ca.ulaval.glo4003.quickflyws.infrastructure.email.exceptions.UnsentEmailException;
import ca.ulaval.glo4003.quickflyws.service.paymentSystem.PaymentSystemService;

public class PaymentSystemResourceApi implements PaymentSystemResource {

  private PaymentSystemService paymentSystemService;

  public PaymentSystemResourceApi(PaymentSystemService paymentSystemService) {
    this.paymentSystemService = paymentSystemService;
  }

  @Override
  public TransactionDetailsDto payTicketsInCart(TransactionToProcessDto transactionToProcessDto) {
    try {
      return paymentSystemService.payTicketsInCart(transactionToProcessDto);

    } catch (UnsentEmailException | BadRequestException e) {
      throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build());
      
    } catch (NotFoundException e) {
      throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build());
    }
  }

}