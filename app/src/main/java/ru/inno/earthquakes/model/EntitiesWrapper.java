package ru.inno.earthquakes.model;

/**
 * @author Artur Badretdinov (Gaket)
 *         05.08.17
 */
public class EntitiesWrapper<T> {

    private State state;
    private T data;

    public EntitiesWrapper(State state, T data) {
        this.state = state;
        this.data = data;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public enum State {
        LOADING,
        SUCCESS,
        ERROR_NETWORK
    }
}
