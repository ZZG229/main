package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CmdTypeCliSyntax.CMDTYPE_PATIENT;
import static seedu.address.logic.parser.CmdTypeCliSyntax.CMDTYPE_SYMPTOM;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.Test;

import seedu.address.logic.commands.FindCommand;

public class FindCommandParserTest {

    private FindCommandParser parser = new FindCommandParser();

    @Test
    public void parse_emptyArgs_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_wrongCommandType_throwsParseException() {
        assertParseFailure(parser, CMDTYPE_SYMPTOM, String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parseValidArgsReturnsFindCommand() {
        FindCommand expectedFindCommand = new FindCommand(CMDTYPE_PATIENT, "search string");
        assertParseSuccess(parser, CMDTYPE_PATIENT + "     search string", expectedFindCommand);
    }

    @Test
    public void parse_findDrugNonAlphabeticalInput_throwsParseException() {
        assertParseFailure(parser, "containsnumbers123", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                FindCommand.MESSAGE_USAGE));
    }
}
