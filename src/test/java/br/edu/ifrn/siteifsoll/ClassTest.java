package br.edu.ifrn.siteifsoll;

import java.text.DateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class ClassTest {
	public static void main(String[] args) {
		Calendar c = Calendar.getInstance();

		Date data = c.getTime();

		DateFormat formataData = DateFormat.getDateInstance();

		System.out.println(formataData.format(data));
	}
}
