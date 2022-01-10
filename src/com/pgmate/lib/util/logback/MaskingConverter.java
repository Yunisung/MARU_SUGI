package com.pgmate.lib.util.logback;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
/**
 * @author Administrator
 *
 */
public class MaskingConverter extends ClassicConverter {

	private static final int MIN_CC_DIGITS = 14;
    private static final String MASK_LABEL = "******";
    private static final int MASK_LABEL_LENGTH = MASK_LABEL.length();
    private static final String[] MASK_LOOKUPS;
    private static final int MASK_LOOKUPS_SIZE = 20;
    private static final String[] CVV		= {"cvv","csc","cvc","cvc2","cve","cid","cvv2","cvn2","cvc1","CVV","CSC","CVC","CVC2","CVE","CID","CVV2","CVN2","CVC1"};
  
    static {
        MASK_LOOKUPS = new String[MASK_LOOKUPS_SIZE];
        for (int i = 0; i < MASK_LOOKUPS.length; i++) {
            MASK_LOOKUPS[i] = buildMask(i);
        }
    }

    @Override
    public String convert(ILoggingEvent e) {
        return mask(remarkCVV(e.getFormattedMessage()));
    }

    static String mask(String formattedMessage) {
        if (!hasEnoughDigits(formattedMessage)) {
            return formattedMessage;
        }

        int length = formattedMessage.length();
        int unwrittenStart = 0;
        int numberStart = -1;
        int numberEnd;
        int digitsSeen = 0;
        int[] last4pos = {-1, -1, -1, -1};
        int pos;
        char current;

        StringBuilder masked = new StringBuilder(formattedMessage.length());

        for (pos = 0; pos < length; pos++) {
            current = formattedMessage.charAt(pos);
            if (isDigit(current)) {
                digitsSeen++;

                if (numberStart == -1) {
                    numberStart = pos;
                }

                last4pos[0] = last4pos[1];
                last4pos[1] = last4pos[2];
                last4pos[2] = last4pos[3];
                last4pos[3] = pos;
            } else if (digitsSeen > 0 && current != ' ' && current != '-') {
                numberEnd = last4pos[3] + 1;
                if ((digitsSeen >= MIN_CC_DIGITS)
                        && luhnCheck(stripSeparators(
                                        formattedMessage.substring(numberStart, numberEnd)))) {
                    masked.append(formattedMessage, unwrittenStart, numberStart);
                    masked.append(getBin(formattedMessage.substring(numberStart, numberEnd))+maskString(
                            formattedMessage.substring(numberStart, numberEnd),
                            formattedMessage.substring(last4pos[0], numberEnd)));
                    masked.append(formattedMessage, last4pos[0], numberEnd);
                    unwrittenStart = numberEnd;
                }
                numberStart = -1;
                digitsSeen = 0;
            }
        }

        if (numberStart != -1 && (digitsSeen >= MIN_CC_DIGITS)
                && luhnCheck(stripSeparators(
                                formattedMessage.substring(numberStart, pos)))) {
            masked.append(formattedMessage, unwrittenStart, numberStart);
            masked.append(getBin(formattedMessage.substring(numberStart, pos))+maskString(
                    formattedMessage.substring(numberStart, pos),
                    formattedMessage.substring(last4pos[0], pos)));
            masked.append(formattedMessage, last4pos[0], pos);
        } else {
            masked.append(formattedMessage, unwrittenStart, pos);
        }

        return masked.toString();
    }

    static boolean hasEnoughDigits(String formattedMessage) {
        if (formattedMessage == null) {
            return false;
        }

        int digits = 0;
        int length = formattedMessage.length();
        char current;

        for (int i = 0; i < length; i++) {
            current = formattedMessage.charAt(i);
            if (isDigit(current)) {
                if (++digits == MIN_CC_DIGITS) {
                    return true;
                }
            } else if (digits > 0 && current != ' ' && current != '-') {
                digits = 0;
            }
        }

        return false;
    }
    
    
    
    static boolean hasCvv(String formattedMessage) {
    	
    	
        if (formattedMessage == null) {
            return false;
        }
        
        if(formattedMessage.indexOf("<cvv>") > 0){
        	
        }
        
        
        
        int digits = 0;
        int length = formattedMessage.length();
        char current;

        for (int i = 0; i < length; i++) {
            current = formattedMessage.charAt(i);
            if (isDigit(current)) {
                if (++digits == MIN_CC_DIGITS) {
                    return true;
                }
            } else if (digits > 0 && current != ' ' && current != '-') {
                digits = 0;
            }
        }

        return false;
    }
    
    
    
    

    /**
     * Implementation of the [Luhn algorithm](http://en.wikipedia.org/wiki/Luhn_algorithm)
     * to check if the given string is possibly a credit card number.
     *
     * @param cardNumber the number to check. It must only contain numeric characters
     * @return `true` if the given string is a possible credit card number
     */
    static boolean luhnCheck(final String cardNumber) {
        int sum = 0;
        int digit, addend;
        boolean doubled = false;
        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            digit = Integer.parseInt(cardNumber.substring(i, i + 1));
            if (doubled) {
                addend = digit * 2;
                if (addend > 9) {
                    addend -= 9;
                }
            } else {
                addend = digit;
            }
            sum += addend;
            doubled = !doubled;
        }
        return (sum % 10) == 0;
    }

    /**
     * Remove any ` ` and `-` characters from the given string.
     *
     * @param cardNumber the number to clean up
     * @return if the given string contains no ` ` or `-` characters, the string
     *      itself is returned, otherwise a new string containing no ` ` or `-`
     *      characters is returned
     */
    static String stripSeparators(final String cardNumber) {
        final int length = cardNumber.length();
        final char[] result = new char[length];
        int count = 0;
        char cur;
        for (int i = 0; i < length; i++) {
            cur = cardNumber.charAt(i);
            if (!(cur == ' ' || cur == '-')) {
                result[count++] = cur;
            }
        }
        if (count == length) {
            return cardNumber;
        }
        return new String(result, 0, count);
    }

    /**
     * Get a mask string for masking the given `fullNum`.
     *
     * @param fullNum the string to be masked
     * @param unmasked the section of `fullNum` to be left unmasked
     * @return a mask string
     */
    static String maskString(String fullNum, String unmasked) {
        final int maskedLength = fullNum.length() - unmasked.length();

        if (maskedLength < MASK_LOOKUPS_SIZE) {
            return MASK_LOOKUPS[maskedLength];
        } else {
            return buildMask(maskedLength);
        }
    }

    /**
     * Create a masking string with the given length. Masks for short lengths
     * are cached at class initialization to minimize calls to this method.
     *
     * @param maskedLength
     * @return a mask string
     */
    static String buildMask(int maskedLength) {
        final int pads = maskedLength - MASK_LABEL_LENGTH;
        StringBuilder mask = new StringBuilder(maskedLength);
        if (pads <= 0) {
            mask.append(MASK_LABEL);
        } else {
        	/*
            for (int i = 0; i < pads / 2; i++) {
                mask.append('*');
            }*/
            mask.append(MASK_LABEL);
            /*
            while (mask.length() < maskedLength) {
                mask.append('*');
            }*/
        }
        return mask.toString();
    }
    
    private static String getBin(String str){
    	if(str.length() < 16){
    		return str.substring(0,5);
    	}else{
    		return str.substring(0,6);
    	}
    }

    private static boolean isDigit(char c) {
        switch (c) {
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8': 
            case '9':
                return true;
            default:
                return false;
        }
    }
    
    private static String remarkCVV(String str){
    	String pattern = "<cvv>([0-9]{3,4})</cvv>";
    	String pattern2 = "\"cvv\": \"([0-9]{3,4})\"";
    	if(str != null){
    		for(String cvv : CVV){
    			Pattern p = Pattern.compile(pattern.replaceAll("cvv", cvv));
    			Matcher m = p.matcher(str);
    			
    			while(m.find()){
    				int startIdx = m.start();
    				int endIdx	 = m.end();
    				String matchStr = str.substring(startIdx, endIdx);
    				int len		= matchStr.length()-(cvv.length()*2)-5;
    				String repStr = "";
    				if(len == 3){ repStr = "<"+cvv+">***</"+cvv+">";}
    				if(len == 4){ repStr = "<"+cvv+">****</"+cvv+">";}
    				str =  str.replaceAll(matchStr, repStr);
    				break;
    			}
    			
    			Pattern p2 = Pattern.compile(pattern2.replaceAll("cvv", cvv));
    			Matcher m2 = p2.matcher(str);
    			while(m2.find()){
    				int startIdx = m2.start();
    				int endIdx	 = m2.end();
    				String matchStr = str.substring(startIdx, endIdx);
    				int len		= matchStr.length()-(cvv.length())-6;
    				String repStr = "";
    				if(len == 3){ repStr = "\""+cvv+"\": \"***\"";}
    				if(len == 4){ repStr = "\""+cvv+"\": \"****\"";}
    				str =  str.replaceAll(matchStr, repStr);
    				break;
    			}
    			
    		}
    	}
    	return str;
    	
    }
    
    
    
    public static void main(String[] args){
    	System.out.println(MaskingConverter.mask("fdf2fff4242424242424242fsfsfs"));
    	System.out.println(MaskingConverter.mask("number=\"44446666444433\""));
    	//String a = "<cvv>^([0-9]{3,4})$</cvv>";
    	//System.out.println("fffff<cvv>12322</cvv>ffff".replaceAll("<cvv>([0-9]{3,4})</cvv>", "<cvv>***</cvv>"));
    	System.out.println(remarkCVV("fffff<cvv>123</cvv>ffff"));
    	System.out.println(remarkCVV("fffff\"cvv\": \"123\",ffff"));
    	System.out.println(remarkCVV("fffff\"cvv\": \"12345\",ffff"));
    	System.out.println(remarkCVV("fffff\"cvv\": \"123*\",ffff"));
    }
}
