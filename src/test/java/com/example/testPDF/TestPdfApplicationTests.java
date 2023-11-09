package com.example.testPDF;

import com.itextpdf.text.DocumentException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.Assert.assertTrue;

@SpringBootTest
class TestPdfApplicationTests {

	@Autowired
	private FileLocationResolver fileLocationResolver;

	@Test
	public void contextLoads() {
		HalfYearlyAccBalanceStatement statement1 = new HalfYearlyAccBalanceStatement();
		statement1.accountOpeningDate="31-Oct-2018";
		statement1.customerCommunicationAddress="UNIT-103,91 D BRIDGE RD,WEST MEAD, SYDNEY, OTHERS, AUSTRALIA";
		statement1.customerPermanentAddress="19, K.K. THANA PARA,KUSTIA, KUSHTIA, KHULNA, BANGLADESH";
		statement1.customerWorkAddress="UNSW.LEVEL-2,AGSM BUILDING UNSW, SYDNEY, OTHERS, AUSTRALIA";
		statement1.accountCurrencyCode="USD";
		statement1.balanceAsOnDate= BigDecimal.valueOf(1924.68);
		statement1.balanceIndicator="CREDIT";
		statement1.solName="AGRABAD BRANCH";
		statement1.branchCode="1101";
		statement1.schemeType="CURRENT AC";
		statement1.balanceConfirmationDate = LocalDate.of(2022,06,30);
		statement1.status = StatementStatus.MAIL_SENT;
		statement1.id =630119L;
		statement1.taskId=-87l;
		statement1.accountNumber="1101200434366001";
		statement1.solId="1101";
		statement1.schemeCode="CAFCY";
		statement1.statementType= StatementType.HALF_YEARLY_ACCOUNT_BALANCE_STATEMENT;
		statement1.cifId="00434366";
		statement1.customerTitle="SUDIPTA KUMAR PAUL";
		statement1.phoneNumber="01703726535";
		statement1.email="SUDIPTA116@GMAIL.COM";
		statement1.insertedAt= LocalDateTime.now();
		statement1.emailSendAt = LocalDateTime.now();
		statement1.year="2022";
		statement1.month="06";
		statement1.isHardcopyRequired=false;

		try {
			new HalfYearlyAccBlncStatementGenerator(statement1, fileLocationResolver.tempDirectoryPath(statement1), false).generatePdf();
		} catch (DocumentException | IOException e) {
			assertTrue("Failed to Generate", false);
		}

		assertTrue("Successfully Generated", true);
	}

}
