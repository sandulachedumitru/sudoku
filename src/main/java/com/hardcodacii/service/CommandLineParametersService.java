package com.hardcodacii.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        var cmdOptions = CmdOptions.initCmdOptions();
        areFormattedParameters(args);
    }

    public void cmdParameterProcessor(String... args) {
        var cmdOptions = CmdOptions.initCmdOptions();

    }

    public static boolean areFormattedParameters(String... args) {

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
            } else {
                System.out.println("NO MATCH !");
                result = false;
            }
        }

        return result;
    }

    private static class CmdOptions {

        private static final HashSet<OptionsDescription> optDescr =new HashSet<>();

        public CmdOptions() {
            initCmdOptions();
        }

        private static HashSet<OptionsDescription> initCmdOptions() {
            OptionsDescription helpOptionDescription = new OptionsDescription();
            helpOptionDescription.setParameterName("help");
            helpOptionDescription.setUsageHelp("");
            helpOptionDescription.setCanHaveValue(false);
            optDescr.add(helpOptionDescription);

            OptionsDescription debugOptionDescription = new OptionsDescription();
            debugOptionDescription.setParameterName("debug");
            debugOptionDescription.setUsageHelp("");
            debugOptionDescription.setCanHaveValue(true);
            optDescr.add(debugOptionDescription);

            OptionsDescription printOptionDescription = new OptionsDescription();
            printOptionDescription.setParameterName("print");
            printOptionDescription.setUsageHelp("");
            printOptionDescription.setCanHaveValue(true);
            optDescr.add(printOptionDescription);

            return optDescr;
        }

        @Setter
        @Getter
        private static class OptionsDescription {
            private String parameterName, usageHelp;
            boolean canHaveValue;
        }
    }
}
