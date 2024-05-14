package it.polimi.ingsw.eventUtils.event.fromModel;

import it.polimi.ingsw.controller.VirtualView;
import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.view.controller.ViewEventReceiver;
import it.polimi.ingsw.viewModel.ViewModel;

/**
 * This class represents an event that updates the local model.
 * It extends the Event class and is used to communicate changes in the model to the view.
 */
public class UpdateLocalModelEvent extends Event {
    /**
     * The unique identifier for this type of event.
     */
    private final static String id = EventID.UPDATE_LOCAL_MODEL.getID();

    /**
     * The ViewModel instance that represents the updated state of the model.
     */
    private final ViewModel model;

    /**
     * Constructs a new UpdateLocalModelEvent with the given ViewModel.
     *
     * @param vm the ViewModel that represents the updated state of the model
     */
    public UpdateLocalModelEvent(ViewModel vm) {
        super(id);
        this.model = vm;
    }

    /**
     * @return the ViewModel that represents the updated state of the model
     */
    public ViewModel getModel() {
        return model;
    }

    @Override
    public void receiveEvent(ViewEventReceiver viewEventReceiver) {
        viewEventReceiver.evaluateEvent(this);
    }

    @Override
    public void receiveEvent(VirtualView virtualView) {
        virtualView.evaluateEvent(this);
    }
}
