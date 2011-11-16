package net.codjo.gui.toolkit.date;
import static net.codjo.gui.toolkit.date.RestrictedDateField.MAX_DATE;
import static net.codjo.gui.toolkit.date.RestrictedDateField.MIN_DATE;
import java.util.Date;
/**
 *
 */
public class DateRange {
    private Date beginDate;
    private Date endDate;


    DateRange(Date beginDate, Date endDate) {
        this.beginDate = beginDate;
        this.endDate = endDate;
    }


    public Date getBeginDate() {
        return beginDate;
    }


    public Date getEndDate() {
        return endDate;
    }


    public boolean contains(Date value) {
        if (beginDate.equals(MIN_DATE)) {
            return value.before(endDate) || endDate.equals(value);
        }
        if (endDate.equals(MAX_DATE)) {
            return value.after(beginDate) || beginDate.equals(value);
        }
        return !(value.before(beginDate) || value.after(endDate));
    }
}
