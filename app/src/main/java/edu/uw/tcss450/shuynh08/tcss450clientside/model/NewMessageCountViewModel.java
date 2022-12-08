package edu.uw.tcss450.shuynh08.tcss450clientside.model;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

/**
 * A ViewModel managing the data for the new-message count.
 */
public class NewMessageCountViewModel extends ViewModel {
    /**
     * Field for the new message count of type MutableLiveData<Integer>.
     */
    private MutableLiveData<Integer> mNewMessageCount;

    /**
     * Constructor for the Register ViewModel. Initializes our mNewMessageCount to 0.
     */
    public NewMessageCountViewModel() {
        mNewMessageCount = new MutableLiveData<>();
        mNewMessageCount.setValue(0);
    }

    /**
     * Used to observe our current message count.
     * @param owner The lifecycle owner
     * @param observer The observer
     */
    public void addMessageCountObserver(@NonNull LifecycleOwner owner,
                                        @NonNull Observer<? super Integer> observer) {
        mNewMessageCount.observe(owner, observer);
    }

    /**
     *
     */
    public void increment() {
        mNewMessageCount.setValue(mNewMessageCount.getValue() + 1);
    }

    public void reset() {
        mNewMessageCount.setValue(0);
    }
}
