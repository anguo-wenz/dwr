package com.onezero.dwr;

import com.onezero.dwr.exception.InvalidDWRResponseException;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DWRResponseDecoder {

    public static void main(String[] args) {
        System.out.println(ARRAY_REGX.matcher("var s0=[]").matches());
        System.out.println(OBJECT_REGX.matcher("var s1= {}").matches());
        System.out.println(ARRAY_PUT_REGX.matcher("s0[0] =true").matches());
        System.out.println(OBJECT_PUT_REGX.matcher("s1.key = \"value\"").matches());

        System.out.println(!ARRAY_REGX.matcher("avar s0=[]").matches());
        System.out.println(!OBJECT_REGX.matcher("var s1=a{}").matches());
        System.out.println(!ARRAY_PUT_REGX.matcher("as0[0]=true").matches());
        System.out.println(!OBJECT_PUT_REGX.matcher("s1a.key=\"value\"").matches());

        System.out.println(TRUE_REGX.matcher("true").matches());
        System.out.println(!TRUE_REGX.matcher("True").matches());

        System.out.println(FALSE_REGX.matcher("FALSE").matches());
        System.out.println(!FALSE_REGX.matcher("False").matches());

        System.out.println(NULL_REGX.matcher("null").matches());
        System.out.println(!NULL_REGX.matcher("Null").matches());

        System.out.println(STRING_REGX.matcher("\"abc\"").matches());
        System.out.println(!STRING_REGX.matcher("Null").matches());

        System.out.println(INTEGER_REGX.matcher("123").matches());
        System.out.println(!INTEGER_REGX.matcher("123a").matches());

        System.out.println(DOUBLE_REGX.matcher("123.0").matches());
        System.out.println(!DOUBLE_REGX.matcher("123.0.0").matches());

        System.out.println(DATE_REGX.matcher("new Date(123456789)").matches());
        System.out.println(!DATE_REGX.matcher("new Date(123456789))").matches());

        System.out.println(REFERENCE_REGX.matcher("s11").matches());
        System.out.println(!REFERENCE_REGX.matcher("ss12").matches());

        System.out.println(BODY_OBJECT_PUT_REGX.matcher("employmentDifferentiatorEnabled:false").matches());

        DWRResponseDecoder decoder = new DWRResponseDecoder();

        System.out.println(decoder.quotesMatch(""));
        System.out.println(decoder.quotesMatch("abc"));
        System.out.println(decoder.quotesMatch("abc\\\""));
        System.out.println(decoder.quotesMatch("\"abc\\\"\""));

        System.out.println(!decoder.quotesMatch("\"abc"));
        System.out.println(!decoder.quotesMatch("\"abc\"\""));
        System.out.println(!decoder.quotesMatch("\""));

        System.out.println(decoder.splitOutOfString("abc:\"abc,abc\",abc", ",").length == 2);
        System.out.println(decoder.splitOutOfString("abc:\"abcabc\",abc", ",").length == 2);
        System.out.println(decoder.splitOutOfString("abc:\"a,b,c,a,b,c\",a,bc", ",").length == 3);

        System.out.println(decoder.decode(null, "\"abc\"").equals("abc"));
        System.out.println(decoder.decode(null, "\"ab\\\"c\"").equals("ab\"c"));
        System.out.println(decoder.decode(null, "123").equals(123));
        System.out.println(decoder.decode(null, "null") == null);
        System.out.println(decoder.decode(null, "123.0").equals(123.0));
        System.out.println(decoder.decode(null, "true"));
        System.out.println(decoder.decode(null, "false").equals(false));

        try {
            decoder.parseValue("employmen:tDifferentiatorEnabled:false", new ArrayList<>());
            System.out.println(false);
        } catch (InvalidDWRResponseException e) {
            System.out.println(true);
        }

        System.out.println();

        System.out.println(decoder.decode(null, "[]"));
        System.out.println(decoder.decode(null, "{}"));
        System.out.println(decoder.decode("var s0={};s0.abc=true;", "[s0]"));
        System.out.println(decoder.decode("var s0=[];s0[0]=true;",
            "{employmentDifferentiatorEnabled:false,navBarGroup:s0,v12ProfileEnabled:true}"));

        System.out.println(decoder.decode(
            "var s0={};var s1={};var s2={};var s3={};var s4={};var s5={};var s6={};var s7={};var s8={};var s9={};var s10={};var s11={};var s12={};var s13={};var s14={};var s15={};var s16={};var s17={};var s18={};var s19={};s0.actionId=null;s0.actionType=\"title\";s0.label=\"Take Action\";s0.url=null;\n" +
                "s1.actionId=\"hrisCha;ngeEmplo\\\"ymentDetailsAction\";s1.actionType=\"popup\";s1.label=\"Change Job and Compensation Info\";s1.url=\"/xi/ui/ect/pages/mss/ectProfileUpdate.xhtml?selected_user=admin&_s.crb=lOz9wVa528UQz5R0uLXzIuf7y4Q%253d\";\n" +
                "s2.actionId=\"hrisPayComponenntNonRecurringAction\";s2.actionType=\"popup\";s2.label=\"Pay Component Non Recurring\";s2.url=\"/xi/ui/ect/pages/mss/ectProfileUpdate.xhtml?selected_user=admin&selectquestion=essMssPayCompNonRecurringActionController&_s.crb=lOz9wVa528UQz5R0uLXzIuf7y4Q%253d\";\n" +
                "s3.actionId=\"hrisEmploymentInfoAction\";s3.actionType=\"popup\";s3.label=\"Employment Details\";s3.url=\"/xi/ui/ect/pages/mss/ectProfileUpdate.xhtml?selected_user=admin&selectquestion=essMssEmploymentDetailsActionController&_s.crb=lOz9wVa528UQz5R0uLXzIuf7y4Q%253d\";\n" +
                "s4.actionId=\"hrisPlanLOAAction\";s4.actionType=\"popup\";s4.label=\"Leave Of Absence\";s4.url=\"/xi/ui/ect/pages/absence/workbench.xhtml?selected_user=admin\";\n" +
                "s5.actionId=\"hrisAddEmploymentInfoAction\";s5.actionType=\"popup\";s5.label=\"Add: Concurrent Employment\";s5.url=\"/xi/ui/ect/pages/mss/ectProfileUpdate.xhtml?selected_user=admin&selectquestion=essMssConcurrentEmploymentActionController&_s.crb=lOz9wVa528UQz5R0uLXzIuf7y4Q%253d\";\n" +
                "s6.actionId=\"hrisAddGlobalAssignmentAction\";s6.actionType=\"popup\";s6.label=\"Add: Global Assignment Details\";s6.url=\"/xi/ui/ect/pages/mss/ectProfileUpdate.xhtml?selected_user=admin&selectquestion=essMssGlobalAssignmentActionController&_s.crb=lOz9wVa528UQz5R0uLXzIuf7y4Q%253d\";\n" +
                "s7.actionId=\"hrisAddPensionPayoutAction\";s7.actionType=\"popup\";s7.label=\"Add: Pension Payout Details\";s7.url=\"/xi/ui/ect/pages/mss/ectProfileUpdate.xhtml?selected_user=admin&selectquestion=essMssPensionPayoutActionController&_s.crb=lOz9wVa528UQz5R0uLXzIuf7y4Q%253d\";\n" +
                "s8.actionId=\"hrisTerminationAction\";s8.actionType=\"popup\";s8.label=\"Terminate\";s8.url=\"/xi/ui/ect/pages/mss/ectProfileUpdate.xhtml?selected_user=admin&selectquestion=essMssTerminateActionController&_s.crb=lOz9wVa528UQz5R0uLXzIuf7y4Q%253d\";\n" +
                "s9.actionId=\"hrisOneTimeDeductionAction\";s9.actionType=\"popup\";s9.label=\"One Time Deduction\";s9.url=\"/xi/ui/ect/pages/mss/ectProfileUpdate.xhtml?selected_user=admin&selectquestion=essMssOneTimeDeductionController&_s.crb=lOz9wVa528UQz5R0uLXzIuf7y4Q%253d\";\n" +
                "s10.actionId=\"hrisDeductionTakeActionRecurringAction\";s10.actionType=\"popup\";s10.label=\"Manage Recurring Deductions\";s10.url=\"/xi/ui/ect/pages/mss/ectProfileUpdate.xhtml?selected_user=admin&selectquestion=essMssRecurringDeductionActionController&_s.crb=lOz9wVa528UQz5R0uLXzIuf7y4Q%253d\";\n" +
                "s11.actionId=\"hrisCostDistributionAction\";s11.actionType=\"popup\";s11.label=\"Manage Alternative Cost Distribution\";s11.url=\"/xi/ui/ect/pages/mss/ectProfileUpdate.xhtml?selected_user=admin&selectquestion=essMssCostDistributionActionController&_s.crb=lOz9wVa528UQz5R0uLXzIuf7y4Q%253d\";\n" +
                "s12.actionId=\"empProfileAddNoteAction\";s12.actionType=\"popup\";s12.label=\"Add Note\";s12.url=\"addNote\";\n" +
                "s13.actionId=\"empProfileGiveBadgeAction\";s13.actionType=\"popup\";s13.label=\"Give a badge\";s13.url=\"addBadge\";\n" +
                "s14.actionId=\"empProfileBIPublisherAction\";s14.actionType=\"popup\";s14.label=\"Create Talent Card\";s14.url=\"showBIPublisherPanel\";\n" +
                "s15.actionId=null;s15.actionType=\"separator\";s15.label=null;s15.url=null;\n" +
                "s16.actionId=null;s16.actionType=\"title\";s16.label=\"Jump To\";s16.url=null;\n" +
                "s17.actionId=\"OrgChart\";s17.actionType=\"pageRedirect\";s17.label=\"Org Chart\";s17.url=\"https://qacand.lab-rot.ondemand.com/sf/orgchart?selected_user=admin&company=ECEPE2EHANA&username=admin\";\n" +
                "s18.actionId=\"CarrerWorksheet\";s18.actionType=\"pageRedirect\";s18.label=\"Career Worksheet\";s18.url=\"https://qacand.lab-rot.ondemand.com/sf/careerworksheet?selected_userid=admin&company=ECEPE2EHANA&username=admin\";\n" +
                "s19.actionId=\"Learning\";s19.actionType=\"pageRedirect\";s19.label=\"Learning\";s19.url=\"/sf/learning?selecteduser=admin\";",
            "[s0,s1,s2,s3,s4,s5,s6,s7,s8,s9,s10,s11,s12,s13,s14,s15,s16,s17,s18,s19]"));

        System.out.println(decoder.decode(
            "var s0=[];var s1={};var s5=[];var s6={};var s7={};var s2={};var s8=[];var s9={};var s11={};var s10={};var s12={};var s3={};var s13=[];var s4={};var s14=[];var s15={};var s17={};var s16={};var s18={};s0[0]=s1;s0[1]=s2;s0[2]=s3;s0[3]=s4;\n" +
                "s1.employmentDifferentiatorEnabledForGroup=false;s1.items=s5;s1.label=\"EMPFILE_HRIS_GA_GROUP_CURRENT\";s1.name=\"EMPFILE_HRIS_GA_GROUP_CURRENT\";\n" +
                "s5[0]=s6;\n" +
                "s6.assignmentPermissionMap=s7;s6.assignmentType=\"ST\";s6.company=null;s6.department=null;s6.employmentDifferentiatorText=null;s6.endDate=\"\";s6.globalAssignmentType=null;s6.hireDate=\"January 1, 2001\";s6.hireDateAsDate=new Date(978307200000);s6.homeAssignment=true;s6.id=\"admin\";s6.jobTitle=\"Analyst\";s6.label=\"Analyst, san\";s6.location=\"san\";s6.mainAssignment=false;s6.plannedEndDate=\"\";s6.status=\"Active\";\n" +
                "\n" +
                "s2.employmentDifferentiatorEnabledForGroup=false;s2.items=s8;s2.label=\"EMPFILE_HRIS_GA_GROUP_PAST\";s2.name=\"EMPFILE_HRIS_GA_GROUP_PAST\";\n" +
                "s8[0]=s9;s8[1]=s10;\n" +
                "s9.assignmentPermissionMap=s11;s9.assignmentType=\"GA\";s9.company=null;s9.department=null;s9.employmentDifferentiatorText=null;s9.endDate=\"September 30, 2013\";s9.globalAssignmentType=\"Long-term assignment\";s9.hireDate=\"September 1, 2013\";s9.hireDateAsDate=new Date(1377993600000);s9.homeAssignment=false;s9.id=\"255\";s9.jobTitle=\"JC BCT Test1 name\";s9.label=\"JC BCT Test1 name\";s9.location=null;s9.mainAssignment=false;s9.plannedEndDate=\"September 30, 2013\";s9.status=\"Ended\";\n" +
                "\n" +
                "s10.assignmentPermissionMap=s12;s10.assignmentType=\"GA\";s10.company=null;s10.department=null;s10.employmentDifferentiatorText=null;s10.endDate=\"October 30, 2013\";s10.globalAssignmentType=\"Short-term assignment\";s10.hireDate=\"October 1, 2013\";s10.hireDateAsDate=new Date(1380585600000);s10.homeAssignment=false;s10.id=\"256\";s10.jobTitle=\"JC BCT Test1 name\";s10.label=\"JC BCT Test1 name, Arlington, Virginia1\";s10.location=\"Arlington, Virginia1\";s10.mainAssignment=false;s10.plannedEndDate=\"October 30, 2013\";s10.status=\"Ended\";\n" +
                "\n" +
                "s3.employmentDifferentiatorEnabledForGroup=false;s3.items=s13;s3.label=\"Pension Payouts\";s3.name=\"Pension Payouts\";\n" +
                "\n" +
                "s4.employmentDifferentiatorEnabledForGroup=false;s4.items=s14;s4.label=null;s4.name=\"EMPFILE_HRIS_GA_GROUP_CURRENT\";\n" +
                "s14[0]=s15;s14[1]=s16;\n" +
                "s15.assignmentPermissionMap=s17;s15.assignmentType=\"PP\";s15.company=\"Ace Denmark\";s15.department=null;s15.employmentDifferentiatorText=null;s15.endDate=\"\";s15.globalAssignmentType=null;s15.hireDate=\"October 9, 2013\";s15.hireDateAsDate=new Date(1381276800000);s15.homeAssignment=false;s15.id=\"257\";s15.jobTitle=\"JC BCT Test1 name\";s15.label=\"JC BCT Test1 name, Ace Denmark\";s15.location=null;s15.mainAssignment=false;s15.plannedEndDate=\"October 17, 2013\";s15.status=\"Active\";\n" +
                "\n" +
                "s16.assignmentPermissionMap=s18;s16.assignmentType=\"PP\";s16.company=\"Ace USA\";s16.department=null;s16.employmentDifferentiatorText=null;s16.endDate=\"\";s16.globalAssignmentType=null;s16.hireDate=\"October 21, 2013\";s16.hireDateAsDate=new Date(1382313600000);s16.homeAssignment=false;s16.id=\"258\";s16.jobTitle=\"JC BCT Test1 name\";s16.label=\"JC BCT Test1 name, Ace USA\";s16.location=null;s16.mainAssignment=false;s16.plannedEndDate=\"October 23, 2013\";s16.status=\"Active\";\n",
            "{employmentDifferentiatorEnabled:false,navBarGroup:s0,v12ProfileEnabled:true,vtest:\"test:test\"}"));

    }

    private DWRResponseDecoder() {

    }

    private static final DWRResponseDecoder INSTANCE = new DWRResponseDecoder();

    public static DWRResponseDecoder getInstance() {
        return INSTANCE;
    }

    private static final Pattern ARRAY_REGX = Pattern.compile("\\s?var\\s+s(\\d+)\\s?=\\s?\\[\\]");

    private static final Pattern OBJECT_REGX = Pattern.compile("\\s?var\\s+s(\\d+)\\s?=\\s?\\{\\}");

    private static final Pattern ARRAY_PUT_REGX = Pattern.compile("\\s?s(\\d+)\\[(\\d+)\\]\\s?=(.+)");

    private static final Pattern OBJECT_PUT_REGX = Pattern.compile("\\s?s(\\d+)\\.([a-zA-Z_]+)\\s?=(.+)");

    private static final Pattern TRUE_REGX = Pattern.compile("TRUE|true");

    private static final Pattern FALSE_REGX = Pattern.compile("FALSE|false");

    private static final Pattern NULL_REGX = Pattern.compile("null");

    private static final Pattern STRING_REGX = Pattern.compile("\".*\"");

    private static final Pattern INTEGER_REGX = Pattern.compile("\\d+");

    private static final Pattern DOUBLE_REGX = Pattern.compile("\\d+\\.\\d+");

    private static final Pattern DATE_REGX = Pattern.compile("new Date\\((\\d+)\\)");

    private static final Pattern REFERENCE_REGX = Pattern.compile("s(\\d+)");

    private static final Pattern BODY_OBJECT_PUT_REGX = Pattern.compile("([^:]+):(.+)");

    private static final String SEMICOLON = ";";

    private static final String COMMA = ",";

    public Object decode(String reply, String body) {
        if (body == null || body.isEmpty()) {
            throw new InvalidDWRResponseException("content cannot be null");
        }

        if (body.startsWith("{")) {
            // response is a JSON Object
            return parseObject(reply, body);
        } else if (body.startsWith("[")) {
            // reponse is a JSON Array
            return parseArray(reply, body);
        } else {
            // other type maybe primitive types or null or String, just return the string, let handler parse it.
            // dwr.engine._remoteHandleCallback('16','0',0);
            // dwr.engine._remoteHandleCallback('16','0',null);
            return parseValue(body, new ArrayList<>());
        }
    }

    private List<Object> parseArray(String reply, String body) {
        //var s0={};
        //dwr.engine._remoteHandleCallback('22','0',[s0]);
        List<Object> references = parseReply(reply);

        // handle simplest way, only depth one
        List<Object> result = new ArrayList<>();
        String[] refs = splitOutOfString(body.substring(1, body.length() - 1), COMMA);
        for (String ref : refs) {
            if (ref == null || ref.isEmpty()) {
                continue;
            }
            Object o = parseValue(ref, references);
            result.add(o);
        }
        return result;
    }

    private Map<String, Object> parseObject(String reply, String body) {
        // var s0=[];
        // dwr.engine._remoteHandleCallback('14','0',{employmentDifferentiatorEnabled:false,navBarGroup:s0,v12ProfileEnabled:true});
        List<Object> references = parseReply(reply);

        // handle simplest way, only depth one
        Map<String, Object> result = new HashMap<>();
        String[] refs = splitOutOfString(body.substring(1, body.length() - 1), COMMA);
        for (String ref : refs) {
            if (ref == null || ref.isEmpty()) {
                continue;
            }
            Matcher matcher = BODY_OBJECT_PUT_REGX.matcher(ref);
            if (matcher.matches()) {
                String field = matcher.group(1);
                String value = matcher.group(2);
                Object obj = parseValue(value, references);
                result.put(field, obj);
            } else {
                throw new InvalidDWRResponseException(ref + " does not match ([^:]+):([.*]+)");
            }
        }

        return result;
    }

    private List<Object> parseReply(String reply) {
        List<Object> references = new ArrayList<>();
        if (reply == null || reply.isEmpty()) {
            return references;
        }

        String[] lines = splitOutOfString(reply, SEMICOLON);
        for (String line : lines) {
            Matcher matcher = ARRAY_REGX.matcher(line);
            if (matcher.matches()) {
                int ref = Integer.parseInt(matcher.group(1));
                setValue(references, ref, new ArrayList<Object>());
                continue;
            }

            matcher = OBJECT_REGX.matcher(line);
            if (matcher.matches()) {
                int ref = Integer.parseInt(matcher.group(1));
                setValue(references, ref, new HashMap<String, Object>());
                continue;
            }

            matcher = ARRAY_PUT_REGX.matcher(line);
            if (matcher.matches()) {
                int ref = Integer.parseInt(matcher.group(1));
                int index = Integer.parseInt(matcher.group(2));
                String value = matcher.group(3);
                Object obj = parseValue(value, references);
                setValue(((ArrayList<Object>) references.get(ref)), index, obj);
                continue;
            }

            matcher = OBJECT_PUT_REGX.matcher(line);
            if (matcher.matches()) {
                int ref = Integer.parseInt(matcher.group(1));
                String field = matcher.group(2);
                String value = matcher.group(3);
                Object obj = parseValue(value, references);
                ((Map<String, Object>) references.get(ref)).put(field, obj);
                continue;
            }
        }
        return references;
    }

    private Object parseValue(String value, List<Object> references) {
        if (value == null || value.isEmpty()) {
            throw new InvalidDWRResponseException("value cannot be null");
        }

        if (NULL_REGX.matcher(value).matches()) {
            return null;
        } else if (TRUE_REGX.matcher(value).matches()) {
            return true;
        } else if (FALSE_REGX.matcher(value).matches()) {
            return false;
        } else if (STRING_REGX.matcher(value).matches()) {
            return value.substring(1, value.length() - 1).replace("\\\"", "\"");
        } else if (INTEGER_REGX.matcher(value).matches()) {
            return Integer.parseInt(value);
        } else if (DOUBLE_REGX.matcher(value).matches()) {
            return Double.parseDouble(value);
        } else {
            Matcher matcher = DATE_REGX.matcher(value);
            if (matcher.matches()) {
                long time = Long.parseLong(matcher.group(1));
                return new Date(time);
            }

            matcher = REFERENCE_REGX.matcher(value);
            if (matcher.matches()) {
                int ref = Integer.parseInt(matcher.group(1));
                return references.get(ref);
            }
            throw new InvalidDWRResponseException("value " + value + " is not valid");

        }
    }

    private void setValue(List<Object> references, int index, Object value) {
        int size = references.size();
        if (size <= index) {
            for (int i = size - 1; i <= index; i++) {
                references.add(null);
            }
        }
        references.set(index, value);
    }

    private String[] splitOutOfString(String value, String split) {
        String[] segments = value.split(split);
        for (int i = 0; i < segments.length; i++) {
            String segment = segments[i];
            if (!quotesMatch(segment)) {
                StringBuilder temp = new StringBuilder(segment);
                temp.append(split);
                for (int j = i + 1; j < segments.length; j++) {
                    String seg = segments[j];
                    if (!quotesMatch(seg)) {
                        temp.append(seg);
                        String[] segments2 = new String[segments.length - j + i];
                        System.arraycopy(segments, 0, segments2, 0, i);
                        segments2[i] = temp.toString();
                        if (j < segments.length - 1) {
                            System.arraycopy(segments, j + 1, segments2, i + 1, segments.length - j - 1);
                        }
                        segments = segments2;
                        break;
                    } else {
                        temp.append(seg).append(split);
                    }
                }
            }
        }
        return segments;
    }

    private boolean quotesMatch(String value) {
        int index = value.indexOf("\"");
        boolean match = true;
        while (index >= 0) {
            if (index > 0) {
                char c = value.charAt(index - 1);
                if (c != '\\') {
                    match = !match;
                }
            } else {
                match = !match;
            }
            index = value.indexOf("\"", index + 1);
        }
        return match;
    }
}
