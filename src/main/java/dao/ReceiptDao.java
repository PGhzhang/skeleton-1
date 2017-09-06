package dao;

import api.ReceiptResponse;
import com.sun.org.apache.regexp.internal.RE;
import generated.tables.records.ReceiptsRecord;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import java.util.ArrayList;
import java.util.HashMap;
import java.math.BigDecimal;
import java.util.List;

import static com.google.common.base.Preconditions.checkState;
import static generated.Tables.RECEIPTS;

public class ReceiptDao {
    DSLContext dsl;
    List<Integer> recs;
    HashMap<String, List<Integer>> tags;


    public ReceiptDao(Configuration jooqConfig) {
        this.dsl = DSL.using(jooqConfig);
        tags = new HashMap<>();
        recs = new ArrayList<>();
    }

    public int insert(String merchantName, BigDecimal amount) {
        ReceiptsRecord receiptsRecord = dsl
                .insertInto(RECEIPTS, RECEIPTS.MERCHANT, RECEIPTS.AMOUNT)
                .values(merchantName, amount)
                .returning(RECEIPTS.ID)
                .fetchOne();

        checkState(receiptsRecord != null && receiptsRecord.getId() != null, "Insert failed");

        recs.add(receiptsRecord.getId());

        return receiptsRecord.getId();
    }

    public List<ReceiptsRecord> getAllReceipts() {
        return dsl.selectFrom(RECEIPTS).fetch();
    }

    public void changeTag(int id, String tag) {
        if (tags.containsKey(tag) == false)
            tags.put(tag, new ArrayList<>());

        List<Integer> tempList = tags.get(tag);
        if (tempList.contains(id)) {
            tempList.remove(tempList.indexOf(id));
        } else
            tempList.add(id);
    }

    public List<ReceiptsRecord> getTag(String tag) {
        List<ReceiptsRecord> change = null;
        if (tags.containsKey(tag)) {
            List<Integer> tempList = tags.get(tag);
            change = new ArrayList<ReceiptsRecord>();
            List<ReceiptsRecord> temp = dsl.selectFrom(RECEIPTS).fetch();
            for (int id : tempList)
                for (ReceiptsRecord re : temp)
                    if (id == re.getId())
                        change.add(re);
        }
        return change;
    }
}

