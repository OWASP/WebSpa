package net.seleucus.wsp.server;

import net.seleucus.wsp.config.InvalidPropertyFileException;
import net.seleucus.wsp.config.WSConfigLoader;
import net.seleucus.wsp.db.WSDatabaseAdaptorYiannis;
import net.seleucus.wsp.db.WSDatabaseManager;
import net.seleucus.wsp.util.WSConstants;
import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListener;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.security.crypto.codec.Base64;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WSLogListener implements TailerListener {


    private final WSDatabaseManager wsDatabaseManager;
    private final Properties config;

    public WSLogListener(final WSConfigLoader wsConfigLoader, final WSDatabaseManager wsDatabaseManager) throws IOException, InvalidPropertyFileException {

        config = wsConfigLoader.getConfiguration("");
        this.wsDatabaseManager = wsDatabaseManager;

    }

    @Override
    public void fileNotFound() {
        // TODO Auto-generated method stub

    }

    @Override
    public void fileRotated() {
        // TODO Auto-generated method stub

    }

    @Override
    public void handle(final String requestLine) {

        // Check if the line length is more than 65535 chars
        if (requestLine.length() < WSConstants.MAX_REQUEST_LENGTH) {
            // throw new InvalidWebSpaRequestException("invalid length");
            return;
        }


        // Check if the regex pattern has been found
        Pattern pattern = Pattern.compile(config.getProperty(WSConstants.LOGGING_REGEX_FOR_EACH_REQUEST));
        Matcher matcher = pattern.matcher(requestLine);
        if (!matcher.find()) {
            return;
        }

        // Check if the request is 100 base64 encoded chars in length
        byte[] decodedLineByteArray = Base64.decode(requestLine.getBytes());
        String decodedLine = new String(decodedLineByteArray);

        try {
            WSDatabaseAdaptorYiannis database = wsDatabaseManager.getInstanceOfAdaptor();

            // Check if the request has been seen before in the wsDatabaseManager
            if (database.getCountForRequest(requestLine) > 0) {
                return;
            }
            ;
            database.storeRequest(requestLine);
            // For all the passwords in the wsDatabaseManager, check if we have a valid incoming password
            List<String> passwords = database.getPasswords();
            String providedPassword = extractHashFromRequestLine(requestLine, 1, 51);

            if (!passwords.contains(providedPassword)) {
                return;
            }
            // Having found a correct password, check if it is mapped against an action
            List<String> commands = database.getActionsForPassword(providedPassword);
            if (commands.size() > 1 || commands.size() == 0) {
                return;
            }
            Runtime.getRuntime().exec(commands.get(0));
            // Run the action, by executing the command found in the wsDatabaseManager


        } catch (IOException e) {
            // TODO
        } catch (InvalidPropertyFileException e) {
            //TODO

        }


    }

    @Override
    public void handle(Exception arg0) {

    }

    @Override
    public void init(Tailer arg0) {
        // TODO Auto-generated method stub
    }

    private String extractHashFromRequestLine(String requestLine, int start, int end) {
        byte[] bytes = requestLine.getBytes();
        byte[] result = ArrayUtils.subarray(bytes, start, end);
        return new String(result);
    }

}
