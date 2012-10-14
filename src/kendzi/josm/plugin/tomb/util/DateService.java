/*
 * This software is provided "AS IS" without a warranty of any kind.
 * You use it on your own risk and responsibility!!!
 *
 * This file is shared under BSD v3 license.
 * See readme.txt and BSD3 file for details.
 *
 */

package kendzi.josm.plugin.tomb.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Tomasz Kędziora (Kendzi)
 */
public class DateService {

    SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat v1Format = new SimpleDateFormat("dd-MM-yyyy");
    SimpleDateFormat v2Format = new SimpleDateFormat("dd.MM.yyyy");
    SimpleDateFormat v3Format = new SimpleDateFormat("dd/MM/yyyy");

    String convertDateFormat(String str, SimpleDateFormat inFormat, SimpleDateFormat outFormat) {

        Date parse = parse(str, inFormat);
        if (parse == null) {
            return null;
        }

        String back = format(parse, inFormat);
        if (back == null) {
            return null;
        }
        if (!str.endsWith(back)) {
            return null;
        }

        return format(parse, outFormat);
    }

    public String dateToIso(String str) {
        if (str == null) {
            return null;
        }

        str = str.trim();

        String ret = null;

        ret = convertDateFormat(str, this.v1Format, this.isoFormat);
        if (ret != null) {
            return ret;
        }

        ret = convertDateFormat(str, this.v2Format, this.isoFormat);
        if (ret != null) {
            return ret;
        }

        ret = convertDateFormat(str, this.v3Format, this.isoFormat);
        if (ret != null) {
            return ret;
        }
        return str;
    }

    public String dateToVisible(String str) {
        if (str == null) {
            return null;
        }

        str = str.trim();

        SimpleDateFormat outFormat = this.v2Format;

        String ret = null;

        ret = convertDateFormat(str, this.isoFormat, outFormat);
        if (ret != null) {
            return ret;
        }

        ret = convertDateFormat(str, this.v1Format, outFormat);
        if (ret != null) {
            return ret;
        }

        ret = convertDateFormat(str, this.v3Format, outFormat);
        if (ret != null) {
            return ret;
        }

        return str;
    }

    private String format(Date date, SimpleDateFormat format) {
        try {
            return format.format(date);
        } catch (Exception e) {
            //
        }
        return null;
    }

    Date parse(String in, SimpleDateFormat format) {
        try {
            return format.parse(in);
        } catch (Exception e) {
            //
        }
        return null;
    }
}
