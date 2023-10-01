package solutions.tal.activity.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity(name = "activity")
@Table(name = "ACTIVITIES")
public class ActivityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "TRADE_REF", nullable = false)
    private String tradeRef;

    @Column(name = "ACCOUNT_NAME", nullable = false)
    private String accountName;

    @Column(name = "PRICE", nullable = false)
    private BigDecimal price;

    protected ActivityEntity() {
    }

    public ActivityEntity(String tradeRef, String accountName, BigDecimal price) {
        this.tradeRef = tradeRef;
        this.accountName = accountName;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public String getTradeRef() {
        return tradeRef;
    }

    public String getAccountName() {
        return accountName;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
