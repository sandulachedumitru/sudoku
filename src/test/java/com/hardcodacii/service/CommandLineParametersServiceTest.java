package com.hardcodacii.service;

import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Dumitru Săndulache (sandulachedumitru@hotmail.com)
 */

@RunWith(SpringRunner.class)
class CommandLineParametersServiceTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @DisplayName("check the validity of app arguments")
    @Test
    void cmdParameterProcessor() {

        System.out.println("Test parameters one by one");
        System.out.println("--------------------------");
        Assertions.assertTrue(CommandLineParametersService.areFormattedParameters("--abc"));
        Assertions.assertTrue(CommandLineParametersService.areFormattedParameters("--abc=123bc5de34"));

        Assertions.assertFalse(CommandLineParametersService.areFormattedParameters("-abc"));
        Assertions.assertFalse(CommandLineParametersService.areFormattedParameters("-abc=123bc5de34"));
        Assertions.assertFalse(CommandLineParametersService.areFormattedParameters("--abc012345"));
        Assertions.assertFalse(CommandLineParametersService.areFormattedParameters("--abc="));
        Assertions.assertFalse(CommandLineParametersService.areFormattedParameters("---abc"));
        Assertions.assertFalse(CommandLineParametersService.areFormattedParameters("xyz--abc"));
        Assertions.assertFalse(CommandLineParametersService.areFormattedParameters(" --abc"));
        Assertions.assertFalse(CommandLineParametersService.areFormattedParameters("--abc "));
        Assertions.assertFalse(CommandLineParametersService.areFormattedParameters("-abc=123bc5de34"));

        System.out.println();
        System.out.println("Test multiple parameters at once");
        System.out.println("--------------------------------");
        Assertions.assertTrue(CommandLineParametersService.areFormattedParameters(
                "--abc"
                , "--abc=123bc5de34"
        ));
        Assertions.assertFalse(CommandLineParametersService.areFormattedParameters(
                "-abc"
                , "-abc=123bc5de34"
                , "--abc012345"
                , "--abc="
                , "---abc"
                , "xyz--abc"
                , " --abc"
                , "--abc "
                , "-abc=123bc5de34"
        ));
    }
}
