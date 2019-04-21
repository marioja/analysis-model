package edu.hm.hafner.analysis.parser;

import java.util.function.Consumer;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertj.SoftAssertions;

import static edu.hm.hafner.analysis.assertj.Assertions.*;
import static edu.hm.hafner.analysis.assertj.SoftAssertions.*;

/**
 * Tests the class {@link XlcLinkerParser}.
 */
class XlcLinkerParserTest extends AbstractParserTest {
    private static final String FILE_NAME = "-";

    XlcLinkerParserTest() {
        super("xlc-linker.txt");
    }

    @Override
    protected IssueParser createParser() {
        return new XlcLinkerParser();
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report.get(0))
                .hasSeverity(Severity.WARNING_HIGH)
                .hasCategory("0711-987")
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasMessage("Error occurred while reading file")
                .hasFileName(FILE_NAME);
    }

    /**
     * Parses a string with xlC linker error.
     */
    @Test
    void shouldParseAnotherLinkerError() {
        Report report = parseString("ld: 0711-317 ERROR: Undefined symbol: nofun()");

        assertSingleIssue(report, softly -> {
            softly.assertThat(report.get(0))
                    .hasSeverity(Severity.WARNING_HIGH)
                    .hasCategory("0711-317")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage("Undefined symbol: nofun()")
                    .hasFileName(FILE_NAME);
        });
    }

    /**
     * Parses a string with xlC linker error.
     */
    @Test
    void shouldParseSevereError() {
        Report report = parseString("ld: 0711-634 SEVERE ERROR: EXEC binder commands nested too deeply.");

        assertSingleIssue(report, softly -> {
            softly.assertThat(report.get(0))
                    .hasSeverity(Severity.WARNING_HIGH)
                    .hasCategory("0711-634")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage("EXEC binder commands nested too deeply.")
                    .hasFileName(FILE_NAME);
        });
    }

    /**
     * Parses a string with xlC linker warning.
     */
    @Test
    void shouldParseWarning() {
        Report report = parseString("ld: 0706-012 The -9 flag is not recognized.");

        assertSingleIssue(report, softly -> {
            softly.assertThat(report.get(0))
                    .hasSeverity(Severity.WARNING_LOW)
                    .hasCategory("0706-012")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage("The -9 flag is not recognized.")
                    .hasFileName(FILE_NAME);
        });
    }

    /**
     * Parses a string with xlC linker warning.
     */
    @Test
    void shouldPareAnotherWarning() {
        Report report = parseString("ld: 0711-224 WARNING: Duplicate symbol: dupe");

        assertSingleIssue(report, softly -> {
            softly.assertThat(report.get(0))
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasCategory("0711-224")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage("Duplicate symbol: dupe")
                    .hasFileName(FILE_NAME);
        });
    }

    /**
     * Parses a string with xlC linker informational message.
     */
    @Test
    void shouldParseInformation() {
        Report report = parseString("ld: 0711-345 Use the -bloadmap or -bnoquiet option to obtain more information.");

        assertSingleIssue(report, softly -> {
            softly.assertThat(report.get(0))
                    .hasSeverity(Severity.WARNING_LOW)
                    .hasCategory("0711-345")
                    .hasLineStart(0)
                    .hasLineEnd(0)
                    .hasMessage("Use the -bloadmap or -bnoquiet option to obtain more information.")
                    .hasFileName(FILE_NAME);
        });
    }

    private void assertSingleIssue(final Report report, final Consumer<SoftAssertions> assertion) {
        assertThat(report).hasSize(1);
        assertSoftly(assertion);
    }

    private Report parseString(final String log) {
        Report report = parseStringContent(log);

        assertThat(report).hasSize(1);

        return report;
    }
}

