package net.codjo.gui.toolkit.date;
import static net.codjo.gui.toolkit.date.RestrictedDateField.MAX_DATE;
import static net.codjo.gui.toolkit.date.RestrictedDateField.MIN_DATE;
import static net.codjo.test.common.matcher.JUnitMatchers.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.junit.Test;
/**
 *
 */
public class DateRangeTest {

    @Test
    public void test_maxValue() throws Exception {
        DateRange dateRange = new DateRange(MIN_DATE, convertIntoDate("15/06/2010"));
        assertThat(dateRange.contains(convertIntoDate("15/06/1976")), equalTo(true));
        assertThat(dateRange.contains(convertIntoDate("01/12/0001")), equalTo(true));
        assertThat(dateRange.contains(convertIntoDate("01/12/2011")), equalTo(false));
        assertThat(dateRange.contains(convertIntoDate("15/06/2010")), equalTo(true));
    }


    @Test
    public void test_minValue() throws Exception {
        DateRange dateRange = new DateRange(convertIntoDate("15/06/2008"), MAX_DATE);
        assertThat(dateRange.contains(convertIntoDate("15/06/1976")), equalTo(false));
        assertThat(dateRange.contains(convertIntoDate("01/12/0001")), equalTo(false));
        assertThat(dateRange.contains(convertIntoDate("01/12/2011")), equalTo(true));
        assertThat(dateRange.contains(convertIntoDate("15/06/2010")), equalTo(true));
        assertThat(dateRange.contains(convertIntoDate("15/06/2008")), equalTo(true));
    }


    private static Date convertIntoDate(String dateValue) throws ParseException {
        return new SimpleDateFormat("dd/MM/yyyy").parse(dateValue);
    }
}
