package za.co.empirestate.botspost.preamped;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.StringReader;

/**
 * Created by jojo on 1/23/2015.
 */
public class XmlVendParser {

    public XmlVendParser(){
    }

    public String parseCreditVendRespToToken (String str){
        String token = null;

        try {

            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(new StringReader(str));
            parser.nextTag();
            Log.d("XML PARSER", "parsing started");
            parser.require(XmlPullParser.START_TAG,null,"creditVendResp");

            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }

                String subTag = parser.getName();
                //  Log.d("SUB TAG",  subTag);
                if(subTag.equalsIgnoreCase("creditVendReceipt")){
                    parser.require(XmlPullParser.START_TAG,null,"creditVendReceipt");

                    while (parser.next() != XmlPullParser.END_TAG) {
                        if (parser.getEventType() != XmlPullParser.START_TAG) {
                            continue;
                        }

                        subTag = parser.getName();
                        // Log.d("SUB TAG",  subTag);
                        if(subTag.equalsIgnoreCase("transactions")){

                            parser.require(XmlPullParser.START_TAG,null,"transactions");

                            while (parser.next() != XmlPullParser.END_TAG) {
                                if (parser.getEventType() != XmlPullParser.START_TAG) {
                                    continue;
                                }
                                subTag = parser.getName();
                                //  Log.d("SUB SUB SUB TAG",  subTag);
                                if(subTag.equalsIgnoreCase("tx")){
                                    parser.require(XmlPullParser.START_TAG, null, "tx");
                                    while (parser.next() != XmlPullParser.END_TAG) {
                                        if (parser.getEventType() != XmlPullParser.START_TAG) {
                                            continue;
                                        }
                                        subTag = parser.getName();
                                        //     Log.d("SUB SUB SUB SUB  TAG",  subTag);

                                        if(subTag.equalsIgnoreCase("creditTokenIssue")){
                                            parser.require(XmlPullParser.START_TAG, null, "creditTokenIssue");
                                           // Log.d(" r0:creditTokenIssue first attribute",  parser.getAttributeValue(0));
                                           // if(parser.getAttributeValue(0).equalsIgnoreCase("b0:SaleCredTokenIssue")){
                                                while (parser.next() != XmlPullParser.END_TAG) {
                                                    if (parser.getEventType() != XmlPullParser.START_TAG) {
                                                        continue;
                                                    }
                                                    subTag = parser.getName();
                                                    Log.d("SUB TAG",  subTag);
                                                    if(subTag.equalsIgnoreCase("q1:token")){
                                                        parser.require(XmlPullParser.START_TAG, null, "q1:token");
                                                        while (parser.next() != XmlPullParser.END_TAG) {
                                                            if (parser.getEventType() != XmlPullParser.START_TAG) {
                                                                continue;
                                                            }
                                                            subTag = parser.getName();
                                                            Log.d("SUB SUB  TAG",  subTag);
                                                            if(subTag.equalsIgnoreCase("q1:stsCipher")){
                                                                parser.require(XmlPullParser.START_TAG, null, "q1:stsCipher");
                                                                token = readText(parser);
                                                                Log.d("TOKEN",  token);
                                                                parser.require(XmlPullParser.END_TAG, null, "q1:stsCipher");
                                                            }
                                                        }
                                                    }else {
                                                        skip(parser);
                                                    }
                                                }
                                            //}

                                        }else {
                                            skip(parser);
                                        }

                                    }
                                }else {
                                    skip(parser);
                                }
                            }

                        }else {
                            skip(parser);
                        }
                    }

                }else {
                    skip(parser);
                }
                //  parser.require(XmlPullParser.END_TAG, null, "r0:creditVendResp");
            }
        }catch (XmlPullParserException ex){
            ex.printStackTrace();
            Log.d("XML PARSER","parser failed");
        }catch (IOException ex2){
            Log.d("XML PARSER","failed to next tag");
        }

        return token;
    }

    public String parseCreditVendRespToUnits (String str){
        String units = null;

        try {

            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(new StringReader(str));
            parser.nextTag();
            Log.d("XML PARSER", "parsing units started");
            parser.require(XmlPullParser.START_TAG,null,"creditVendResp");

            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }

                String subTag = parser.getName();
                //  Log.d("SUB TAG",  subTag);
                if(subTag.equalsIgnoreCase("creditVendReceipt")){
                    parser.require(XmlPullParser.START_TAG,null,"creditVendReceipt");

                    while (parser.next() != XmlPullParser.END_TAG) {
                        if (parser.getEventType() != XmlPullParser.START_TAG) {
                            continue;
                        }

                        subTag = parser.getName();
                        // Log.d("SUB TAG",  subTag);
                        if(subTag.equalsIgnoreCase("transactions")){

                            parser.require(XmlPullParser.START_TAG,null,"transactions");

                            while (parser.next() != XmlPullParser.END_TAG) {
                                if (parser.getEventType() != XmlPullParser.START_TAG) {
                                    continue;
                                }
                                subTag = parser.getName();
                                //  Log.d("SUB SUB SUB TAG",  subTag);
                                if(subTag.equalsIgnoreCase("tx")){
                                    parser.require(XmlPullParser.START_TAG, null, "tx");
                                    while (parser.next() != XmlPullParser.END_TAG) {
                                        if (parser.getEventType() != XmlPullParser.START_TAG) {
                                            continue;
                                        }
                                        subTag = parser.getName();
                                        //     Log.d("SUB SUB SUB SUB  TAG",  subTag);

                                        if(subTag.equals("creditTokenIssue")){
                                            parser.require(XmlPullParser.START_TAG, null, "creditTokenIssue");
                                            while (parser.next() != XmlPullParser.END_TAG) {
                                                if (parser.getEventType() != XmlPullParser.START_TAG) {
                                                    continue;
                                                }
                                                        subTag = parser.getName();
                                                        Log.d("SUB SUB  TAG",  subTag);
                                                        if(subTag.equalsIgnoreCase("q1:units")){
                                                            parser.require(XmlPullParser.START_TAG, null, "q1:units");
                                                            units = parser.getAttributeValue(null, "value");
                                                            Log.d("UNITS",  units);
                                                         //   parser.require(XmlPullParser.END_TAG, null, "q2:units");
                                                        }else {
                                                            skip(parser);
                                                        }
                                            }

                                        }else {
                                            skip(parser);
                                        }

                                    }
                                }else {
                                    skip(parser);
                                }
                            }

                        }else {
                            skip(parser);
                        }
                    }

                }else {
                    skip(parser);
                }
            }
        }catch (XmlPullParserException ex){
            ex.printStackTrace();
            Log.d("XML PARSER","parser failed");
        }catch (IOException ex2){
            Log.d("XML PARSER","failed to next tag");
        }

        return units;
    }

    public String[] getTokenAndUnits(String str){
        String[] reciptAr = new String[2];

        reciptAr[0]= parseCreditVendRespToToken(str);
        reciptAr[1]=parseCreditVendRespToUnits(str);

        return reciptAr;
    }

    public String parseCreditVendTrialResp (String resp){
        String msg = null;
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(new StringReader(resp));
            parser.nextTag();
            Log.d("XML PARSER", "parsing fault msg started");

            parser.require(XmlPullParser.START_TAG,null,"soap:Fault");

            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                String subTag = parser.getName();

                if(subTag.equals("faultstring")){

                    parser.require(XmlPullParser.START_TAG,null,"faultstring");
                    msg = readText(parser);
                    Log.d("MSg",  msg);
                    parser.require(XmlPullParser.END_TAG, null, "faultstring");

                }else{
                    skip(parser);
                }
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return msg;
    }

    public String parsePaymentResponse(String str){
        String resp = null;


        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(new StringReader(str));
            parser.nextTag();
            Log.d("XML PARSER", "parsing payment resp started");


            parser.require(XmlPullParser.START_TAG,null,"AuthorisationResponse");

            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                String subTag = parser.getName();
                if(subTag.equals("Response")){

                    parser.require(XmlPullParser.START_TAG,null,"Response");
                    resp = readText(parser);
                    Log.d("MSg",resp);
                    parser.require(XmlPullParser.END_TAG, null, "Response");

                }else{
                    skip(parser);
                }
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return resp;
    }

    private String readText(XmlPullParser parser) throws IOException,
            XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
