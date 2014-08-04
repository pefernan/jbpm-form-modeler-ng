package org.jbpm.formModeler.ng.services.context.impl.marshalling.fieldMarshallers;

import org.apache.commons.lang.StringUtils;
import org.jbpm.formModeler.ng.model.FieldValueMarshaller;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.ParsePosition;

public class NumberMarshaller implements FieldValueMarshaller {
    private String desiredClassName;
    private String pattern;

    public String getDesiredClassName() {
        return desiredClassName;
    }

    public void setDesiredClassName(String desiredClassName) {
        this.desiredClassName = desiredClassName;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public String marshallValue(Object value) {
        if (value == null) return "";
        if (desiredClassName.equals(Double.class.getName()) || desiredClassName.equals("double") ||
                desiredClassName.equals(Float.class.getName())  || desiredClassName.equals("float") ||
                desiredClassName.equals(BigDecimal.class.getName())) {

            DecimalFormat format = new DecimalFormat(pattern);
            return format.format(value);
        }
        return value.toString();
    }

    @Override
    public Object unMarshallValue(String marshalledValue) {
        if (StringUtils.isEmpty(marshalledValue)) return null;

        if (desiredClassName.equals("byte")) {
            return Byte.decode(marshalledValue);
        } else if (desiredClassName.equals("short")) {
            return Short.decode(marshalledValue);
        } else if (desiredClassName.equals("int")) {
            return Integer.decode(marshalledValue);
        } else if (desiredClassName.equals("long")) {
            return Long.decode(marshalledValue);
        } else if (desiredClassName.equals(Byte.class.getName())) {
            return Byte.decode(marshalledValue);
        } else if (desiredClassName.equals(Short.class.getName())) {
            return Short.decode(marshalledValue);
        } else if (desiredClassName.equals(Integer.class.getName())) {
            return Integer.decode(marshalledValue);
        } else if (desiredClassName.equals(Long.class.getName())) {
            return Long.decode(marshalledValue);
        } else if (desiredClassName.equals(Double.class.getName()) || desiredClassName.equals("double") ||
                desiredClassName.equals(Float.class.getName())  || desiredClassName.equals("float") ||
                desiredClassName.equals(BigDecimal.class.getName())) {

            DecimalFormat df = new DecimalFormat(pattern);
            if (desiredClassName.equals(BigDecimal.class.getName())) df.setParseBigDecimal(true);
            ParsePosition pp = new ParsePosition(0);
            Number num = df.parse(marshalledValue, pp);

            if (desiredClassName.equals(BigDecimal.class.getName())) {
                return num;
            } else if (desiredClassName.equals(Float.class.getName()) || desiredClassName.equals("float")) {
                return new Float(num.floatValue());
            } else if (desiredClassName.equals(Double.class.getName()) || desiredClassName.equals("double")) {
                return new Double(num.doubleValue());
            }
        }  else if (desiredClassName.equals(BigInteger.class.getName())) {
            return new BigInteger(marshalledValue);

        }
        return null;
    }
}
