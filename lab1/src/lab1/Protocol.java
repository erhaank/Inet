//package lab1;

public class Protocol {
	private ConnectionState currentState;

	public Protocol() {
		currentState = ConnectionState.OPTIONS;
	}

	public void setState(ConnectionState state) {
		currentState = state;
	}

	public ConnectionState getState() {
		return currentState;
	}
}
