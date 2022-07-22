package com.hardcodacii.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Dumitru Săndulache (sandulachedumitru@hotmail.com)
 */

@Service
@RequiredArgsConstructor
public class CommandLineParametersService {


    public static void main(String... args) {
        System.out.println(CmdOptions.sanitizeSingleSupportedOptionValue("-=ena bl -e+"));
        System.out.println(CmdOptions.sanitizeSingleSupportedOptionValue("disable"));

        CmdOptions.initCmdOptions();
        System.out.println("optDesc size: " + CmdOptions.optDesc.size());
        for (var od : CmdOptions.optDesc) {

            System.out.println("cmd options: " + od);
        }

//        cmdParameterProcessor(args);

    }

    public static void cmdParameterProcessor(String... args) {
        areFormattedParameters(args);
    }

    public static boolean areFormattedParameters(String... args) {

        String regexForCmdParameters = "^([a-zA-Z0-9]+)(\\.[a-zA-Z0-9]+)?$";
        String regexForCmdOptions = "^(--[a-zA-Z]+[^0-9\\W])(=(\\w+))?$";

        Pattern pattern = Pattern.compile(regexForCmdOptions);

        boolean result = true;
        for (String arg : args) {
            System.out.print("arg: " + arg + "\t");
            Matcher matcher = pattern.matcher(arg);
            if (matcher.find()) {
                System.out.print("Start index: " + matcher.start());
                System.out.print(" End index: " + matcher.end() + "\t");
                System.out.println("groupCount: " + matcher.groupCount());
                System.out.println("\t\tgroup()--> " + matcher.group());
                for (int g = 0; g <= matcher.groupCount(); g++) {
                    System.out.println("\t\tgroup[" + g + "]--> " + matcher.group(g));
                }
                if (matcher.group(2) == null && matcher.group(3) == null) {

                }
            } else {
                System.out.println("NO MATCH !");
                result = false;
            }
        }

        return result;
    }

    @Getter
    private static class CmdOptions {

        private static final HashSet<OptionsDescription> optDesc =new HashSet<>();

        private static void initCmdOptions() {
            // --help
            storeOptionsProperties("help", null, false, false);

            // --debug
            String[] supportedValues = new String[] {"disable", "enable"};
            storeOptionsProperties("debug", supportedValues, true, false);

            // --printTo
            storeOptionsProperties("printTo", null, true, true);
        }

        private static void storeOptionsProperties(
                String optionName,
                String[] supportedValues,
                boolean supportOtherOptions,
                boolean canHaveValue
        ) {
            String appName = "sudoku";
            String prefix = "--";

            String _optionName, _usageHelp = "", _messageSignalsTheUseOfDefaultValue;
            String _defaultSupportedValue;
            String[] _supportedValues;

            if (optionName == null) {
                System.out.println("Option name is null. Options will not be supported by CLI");
                return;
            } _optionName = sanitizeSingleSupportedOptionValue(optionName);

            _supportedValues = sanitizeAllSupportedOptionValues(supportedValues);
            if (_supportedValues != null) {
                _defaultSupportedValue = _supportedValues[0];
                _messageSignalsTheUseOfDefaultValue = "The default value for option [" + _optionName + "] was set to [" + _defaultSupportedValue + "]";
                for (var sv : _supportedValues) {
                    _usageHelp += prefix + _optionName + "=" + sv + " OR ";
                }
                _usageHelp += prefix + _optionName; // sudoku --debug
            } else {
                _messageSignalsTheUseOfDefaultValue = "The option [" + _optionName + "] do not support value";
                _usageHelp = appName + " " + prefix + _optionName; // sudoku --debug
            }

            OptionsDescription optionDescription = new OptionsDescription();
            optionDescription.optionName = _optionName;
            optionDescription.usageHelp = _usageHelp;
            optionDescription.messageSignalsTheUseOfDefaultValue = _messageSignalsTheUseOfDefaultValue;
            optionDescription.canHaveValue = canHaveValue;
            optionDescription.supportedValues = _supportedValues;
            optionDescription.supportOtherOptions = supportOtherOptions;
            optDesc.add(optionDescription);
        }

        private static boolean isStringNullOrEmpty(String str) {
            String strTrim = str.trim();
            return strTrim == null || strTrim.equals("");
        }

        private static boolean isArrayNullOrEmpty(String[] strArray) {
            return strArray == null || strArray.length == 0;
        }

        private static String[] sanitizeAllSupportedOptionValues(String[] supportedOptions) {
            if (isArrayNullOrEmpty(supportedOptions)) return null;

            String[] sanitizedSupportedOptions = new String[supportedOptions.length];
            int paramCount = 1;
            for (var s = 0; s < supportedOptions.length; s++) {
                if (supportedOptions[s] == null || supportedOptions[s].equals(""))
                    sanitizedSupportedOptions[s] = "value" + paramCount++;
                else
                    sanitizedSupportedOptions[s] = sanitizeSingleSupportedOptionValue(supportedOptions[s]);
            }

            return sanitizedSupportedOptions;
        }

        private static String sanitizeSingleSupportedOptionValue(String value) {
            String regex = "[a-zA-Z0-9]+";

            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(value);

            StringBuilder sb = new StringBuilder();
            while (matcher.find())
                sb.append(matcher.group());

            return sb.length() == 0 ? value : sb.toString();
        }

        @Getter
        @ToString
        private static class OptionsDescription {
            private String optionName, usageHelp, messageSignalsTheUseOfDefaultValue;
            boolean canHaveValue; // used by options that expect user input value (ex: --print=MyFile.txt )
            boolean supportOtherOptions; // ex --debug=enable
            String[] supportedValues;
        }
    }
}
