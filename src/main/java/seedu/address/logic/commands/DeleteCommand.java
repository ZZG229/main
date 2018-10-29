package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CmdTypeCliSyntax.CMDTYPE_APPOINTMENT;
import static seedu.address.logic.parser.CmdTypeCliSyntax.CMDTYPE_PATIENT;
import static seedu.address.model.AddressBookModel.PREDICATE_SHOW_ALL_EXISTING_PERSONS;
import static seedu.address.model.ScheduleModel.PREDICATE_SHOW_SCHEDULE_EVENTS;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.events.ui.SwitchToPatientEvent;
import seedu.address.commons.events.ui.SwitchToScheduleEvent;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBookModel;
import seedu.address.model.DiagnosisModel;
import seedu.address.model.ScheduleModel;
import seedu.address.model.event.EventId;
import seedu.address.model.event.exceptions.ScheduleEventNotFoundException;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonId;
import seedu.address.model.person.exceptions.PersonNotFoundException;

/**
 * Deletes a person identified using it's displayed index from the address book.
 */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the patient or appointment identified by their ID.\n"
            + "Parameters: commandType(patient, appointment), patientID/appointment ID\n"
            + "Example: " + COMMAND_WORD + " patient p/54103";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted Person: %1$s";
    public static final String MESSAGE_DELETE_EVENT_SUCCESS = "Deleted Event: %1$s";

    private String cmdType;
    private String target;

    public DeleteCommand(String cmdType, String target) {
        this.cmdType = cmdType;
        this.target = target;
    }

    @Override
    public CommandResult execute(AddressBookModel addressBookModel, ScheduleModel scheduleModel,
                                 DiagnosisModel diagnosisModel, CommandHistory history) throws CommandException {
        requireNonNull(addressBookModel);
        requireNonNull(scheduleModel);
        // requireNonNull(diagnosisModel); // no diagnosis model delete

        if (cmdType.equals(CMDTYPE_PATIENT)) {
            if (!PersonId.isValidId(target)) {
                throw new CommandException("Incorrect format for patient ID.");
            }
            try {
                Person foundPerson = addressBookModel.getPersonById(new PersonId(target));
                addressBookModel.deletePerson(foundPerson);
                addressBookModel.updateFilteredPersonList(PREDICATE_SHOW_ALL_EXISTING_PERSONS);
            } catch (PersonNotFoundException e) {
                throw new CommandException("Person ID " + target + " not found");
            }
            EventsCenter.getInstance().post(new SwitchToPatientEvent());
            return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, target));
        } else if (cmdType.equals(CMDTYPE_APPOINTMENT)) {
            if (!EventId.isValidId(target)) {
                throw new CommandException("Incorrect format for event ID.");
            }
            try {
                scheduleModel.deleteEvent(scheduleModel.getEventById(new EventId(target)));
                scheduleModel.updateFilteredEventList(PREDICATE_SHOW_SCHEDULE_EVENTS);
            } catch (ScheduleEventNotFoundException e) {
                throw new CommandException("Event ID " + target + " not found");
            }
            EventsCenter.getInstance().post(new SwitchToScheduleEvent());
            return new CommandResult(String.format(MESSAGE_DELETE_EVENT_SUCCESS, target));
        } else {
            throw new CommandException("Unexpected values for cmdTYpe: should have been caught in parser.");
        }

    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DeleteCommand // instanceof handles nulls
                && cmdType.equals(((DeleteCommand) other).cmdType)
                && target.equals(((DeleteCommand) other).target)); // state check
    }
}
