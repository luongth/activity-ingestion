package solutions.tal.activity.model;

import com.ancientprogramming.fixedformat4j.annotation.Align;
import com.ancientprogramming.fixedformat4j.annotation.Field;
import com.ancientprogramming.fixedformat4j.annotation.FixedFormatDecimal;
import com.ancientprogramming.fixedformat4j.annotation.Record;

import java.math.BigDecimal;

@Record
public class ActivityFileRecord {

    private String tradeRef;

    private String account;

    private BigDecimal price;

    @Field(offset = 1, length = 13)
    public String getTradeRef() {
        return tradeRef;
    }

    public void setTradeRef(String tradeRef) {
        this.tradeRef = tradeRef;
    }

    @Field(offset = 14, length = 22)
    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    @Field(offset = 36, length = 10, align = Align.RIGHT)
    @FixedFormatDecimal(decimals = 3, useDecimalDelimiter = true)
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "ActivityFileRecord{" +
                "tradeRef='" + tradeRef + '\'' +
                ", account='" + account + '\'' +
                ", price=" + price +
                '}';
    }
}
