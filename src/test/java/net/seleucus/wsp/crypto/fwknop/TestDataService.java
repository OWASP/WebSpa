package net.seleucus.wsp.crypto.fwknop;

import net.seleucus.wsp.crypto.fwknop.fields.DigestType;
import net.seleucus.wsp.crypto.fwknop.fields.HmacType;
import net.seleucus.wsp.crypto.fwknop.fields.MessageType;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class TestDataService {

    public static final String TEST_DATA_FILE = "/data/fwknop/test_data.csv";

    public static List<TestDataRow> getTestData() throws Exception {
        final List<TestDataRow> rows = new ArrayList<>();
        for(String line : IOUtils.readLines(TestDataService.class.getResourceAsStream(TEST_DATA_FILE))){
            final String[] fields = StringUtils.split(line, ';');

            final TestDataRow testDataRow = new TestDataRow();
            testDataRow.setEncryptionKey(Hex.decodeHex(fields[0].toCharArray()));
            testDataRow.setSignatureKey(Hex.decodeHex(fields[1].toCharArray()));
            testDataRow.setDigestType(DigestType.valueOf(fields[2]));
            testDataRow.setHmacType(HmacType.valueOf("Hmac" + fields[3]));
            testDataRow.setRandomValue(Long.valueOf(fields[4]));
            testDataRow.setUserName(fields[5]);
            testDataRow.setTimestamp(Long.valueOf(fields[6]));
            testDataRow.setVersion(fields[7]);
            testDataRow.setMessageType(MessageType.values()[Integer.valueOf(fields[8])]);
            testDataRow.setMessage(fields[9]);
            testDataRow.setDigest(fields[10]);
            testDataRow.setEncodedData(fields[11]);

            final String spaDataAndHmac = fields[12];
            testDataRow.setSpaData(spaDataAndHmac.substring(0, spaDataAndHmac.length() - testDataRow.hmacType().base64Length()));
            testDataRow.setHmac(spaDataAndHmac.substring(spaDataAndHmac.length() - testDataRow.hmacType().base64Length()));
            rows.add(testDataRow);
        }

        return rows;
    }

}
