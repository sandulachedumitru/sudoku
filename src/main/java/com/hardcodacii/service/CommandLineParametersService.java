package com.hardcodacii.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Dumitru Săndulache (sandulachedumitru@hotmail.com)
 */

@Service
@RequiredArgsConstructor
public class CommandLineParametersService {

    private static final String APP_NAME = "sudoku";
    private static final String CMD_OPTION_PREFIX = "--";


    public static void main(String... args) {
        System.out.println(CmdOptions.sanitizeSingleSupportedOptionValue("-=ena bl -e+"));
        System.out.println(CmdOptions.sanitizeSingleSupportedOptionValue("disable"));

//        String[] arguments = {
//                "abc",
//                "-debug",
//                "--debug",
//                "-debug=",
//                "--debug=",
//                "-debug=enable",
//                "--debug=enable",
//                "---debug=enable",
//                "--debug1=enable",
//                "--debug=.enable",
//                "MySudokyFile.txt",
//                "--help",
//                "--help=true"
//        };

//        String[] arguments = {
//                "--debug",
//                "--debug=enable",
//                "--debug=enable",
//                "MySudokyFile.txt",
//                "--help",
//                "--help=true"
//        };

        String[] arguments = {
                "-abc",
                "--skipTest=true",
                "--debug=true",
                "--debug",
                "--debug=enable",
                "MySudokyFile.txt",
                "--help=true",
                "--help"
        };

        cmdParameterProcessor(arguments);
    }

    public static void cmdParameterProcessor(String... args) {
        // definitions and initialization of the supported cmd options
        CmdOptions.defineCmdOptions();
        System.out.println("optDesc size: " + CmdOptions.optDesc.size());
        for (var od : CmdOptions.optDesc)
            System.out.println("cmd options: " + od);

        // analyzing the app arguments
        var areFormatted = areAllParametersFormatted(args);
        System.out.println("areAllParametersFormatted(args): " + areFormatted);
        System.out.println("parsedArgumentsInfo: ");
        for (var argsInfo : CmdArgsParser.parsedArgumentsInfo)
            System.out.println("\t" + argsInfo);
        System.out.println("Number of occurrences for:");
        for (var entry : CmdArgsParser.numberOfOccurrences.entrySet())
            System.out.println("\t[arg:" + entry.getKey() + "]\t[occurrences:" + entry.getValue() + "]");

        // perform action

    }

    public static boolean areAllParametersFormatted(String... args) {
        var regexForCmdOptions = "^(--([a-zA-Z]+[^0-9\\W]))(=(\\w+))?$";
        var regexForCmdParameters = "^(\\w+)(\\.([a-zA-Z0-9]+))?$";
        //String regexForCmdParameters = "^([a-zA-Z0-9]+)(\\.([a-zA-Z0-9]+))?$";

        var patternCmdOption = Pattern.compile(regexForCmdOptions);
        var patternCmdParam = Pattern.compile(regexForCmdParameters);

        boolean result = true;
        for (var arg : args) {
            var info = new CmdArgsParser.CmdArgsInfo();

            info.cmdArgument = arg;

            System.out.print("arg: " + arg + "\t");
            var matcherCmdOption = patternCmdOption.matcher(arg);
            var matcherCmdParam = patternCmdParam.matcher(arg);

            if (matcherCmdOption.find()) {
                    /*
                        arg: --debug=enable	Start index: 0 End index: 14	groupCount: 4
                            group()--> --debug=enable
                            group[0]--> --debug=enable
                            group[1]--> --debug
                            group[2]--> debug
                            group[3]--> =enable
                            group[4]--> enable
                    */
                System.out.print("Start index: " + matcherCmdOption.start());
                System.out.print(" End index: " + matcherCmdOption.end() + "\t");
                System.out.println("groupCount: " + matcherCmdOption.groupCount());
                System.out.println("\t\tOPTION: group()--> " + matcherCmdOption.group());
                for (int g = 0; g <= matcherCmdOption.groupCount(); g++) {
                    System.out.println("\t\tOPTION: group[" + g + "]--> " + matcherCmdOption.group(g));
                }

                CmdArgsParser.CmdArgumentProperties properties = new CmdArgsParser.CmdArgumentProperties();
                properties.isOption = true;
                properties.name = matcherCmdOption.group(2);
                properties.value = matcherCmdOption.group(4);

                info.hasValidPattern = true;
                info.isSupported = CmdOptions.optDesc.stream().anyMatch(od -> od.optionName.equals(properties.name));
                info.properties = properties;
            } else if (matcherCmdParam.find()) {
                    /*
                        arg: MySudokyFile.txt	Start index: 0 End index: 16	groupCount: 3
                            PARAMETER: group()--> MySudokyFile.txt
                            PARAMETER: group[0]--> MySudokyFile.txt
                            PARAMETER: group[1]--> MySudokyFile
                            PARAMETER: group[2]--> .txt
                            PARAMETER: group[3]--> txt
                    */
                System.out.print("Start index: " + matcherCmdParam.start());
                System.out.print(" End index: " + matcherCmdParam.end() + "\t");
                System.out.println("groupCount: " + matcherCmdParam.groupCount());
                System.out.println("\t\tPARAMETER: group()--> " + matcherCmdParam.group());
                for (int g = 0; g <= matcherCmdParam.groupCount(); g++) {
                    System.out.println("\t\tPARAMETER: group[" + g + "]--> " + matcherCmdParam.group(g));
                }

                CmdArgsParser.CmdArgumentProperties properties = new CmdArgsParser.CmdArgumentProperties();
                properties.isOption = false;
                properties.name = matcherCmdParam.group();
                properties.value = null;

                info.hasValidPattern = true;
                info.isSupported = false;
                info.properties = properties;
            } else {
                System.out.println("NO MATCH !");

                info.hasValidPattern = false;
                info.isSupported = false;
                info.properties = null;

                result = false;
            }

            if (info.properties != null)
                if (CmdArgsParser.parsedArgumentsInfo.contains(info)) {
                    System.out.println("already exists: " + info.properties.name + "\n");
                    int numOfOcc = CmdArgsParser.numberOfOccurrences.get(info.properties.name);
                    CmdArgsParser.numberOfOccurrences.put(
                            info.properties.name,
                            ++numOfOcc
                    );
                } else CmdArgsParser.numberOfOccurrences.put(info.properties.name, 1);

            CmdArgsParser.parsedArgumentsInfo.add(info);
        }

        return result;
    }

    @Getter
    private static class CmdArgsParser {
        private static final ArrayList<CmdArgsInfo> parsedArgumentsInfo = new ArrayList<>();
        private static final Map<String, Integer> numberOfOccurrences = new HashMap<>();

        @Getter
        @ToString
        private static class CmdArgsInfo {
            private String cmdArgument;
            private boolean hasValidPattern = false;
            private boolean isSupported = false; // relevant only for cmd options
            private CmdArgumentProperties properties;

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                CmdArgsInfo that = (CmdArgsInfo) o;
                return Objects.equals(properties, that.properties);
            }

            @Override
            public int hashCode() {
                return Objects.hash(properties);
            }
        }

        @Getter
        @ToString
        private static class CmdArgumentProperties {
            private boolean isOption = false; // true if cmd option (ex: --help); false if parameter (ex; SudokuInput.txt)
            private String name;
            private String value;

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                CmdArgumentProperties that = (CmdArgumentProperties) o;
                return isOption == that.isOption && Objects.equals(name, that.name);
            }

            @Override
            public int hashCode() {
                return Objects.hash(isOption, name);
            }
        }
    }

    @Getter
    private static class CmdOptions {

        private static final HashSet<OptionsDescription> optDesc = new HashSet<>();

        private static void defineCmdOptions() {
            // --help
            storeOptionsProperties("help", null, false, false);

            // --debug
            String[] supportedValues = new String[]{"disable", "enable"};
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
            String _optionName, _usageHelp = "", _messageSignalsTheUseOfDefaultValue;
            String _defaultSupportedValue;
            String[] _supportedValues;

            if (optionName == null) {
                System.out.println("Option name is null. Options will not be supported by CLI");
                return;
            }
            _optionName = sanitizeSingleSupportedOptionValue(optionName);

            _supportedValues = sanitizeAllSupportedOptionValues(supportedValues);
            if (_supportedValues != null) {
                _defaultSupportedValue = _supportedValues[0];
                _messageSignalsTheUseOfDefaultValue = "The default value for option [" + _optionName + "] was set to [" + _defaultSupportedValue + "]";
                for (var sv : _supportedValues) {
                    _usageHelp += CMD_OPTION_PREFIX + _optionName + "=" + sv + " OR ";
                }
                _usageHelp += CMD_OPTION_PREFIX + _optionName; // sudoku --debug
            } else {
                _messageSignalsTheUseOfDefaultValue = "The option [" + _optionName + "] do not support value";
                _usageHelp = APP_NAME + " " + CMD_OPTION_PREFIX + _optionName; // sudoku --debug
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
