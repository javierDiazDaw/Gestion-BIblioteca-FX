package gui;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javafx.application.Platform;

public class Notifications {
	
	public final static String CATALOGO_UPDATED = "catalogoUpdated";
	
	private static final Map<String , List<SubscriberObject>> subscribers = new LinkedHashMap<>();

	private static Notifications instance = new Notifications();
	
	private Notifications() {
		
	}
	
	
	public static void publish(String event) {
		Platform.runLater( () -> {
			List<SubscriberObject> subscriberList = instance.subscribers.get(event);
			
				if(subscriberList!=null) {
					subscriberList.forEach(
						subscriberObject -> subscriberObject.getCallBack().accept(event)
					);
				}
			}		
		);
	}
	
	public static void subscribe(String event, Object subscriber, Consumer<String> callback) {
		if( !instance.subscribers.containsKey(event)) {
			List<SubscriberObject> sList = new ArrayList<Notifications.SubscriberObject>();
			instance.subscribers.put(event, sList);
		}
		
		List<SubscriberObject> subscriberList = instance.subscribers.get(event);
		subscriberList.add(new SubscriberObject(subscriber, callback));
	}
	
	public static void unSubscribe(String event, Object subscriber) {
		List<SubscriberObject> subscriberList = instance.subscribers.get(event);
		
		if(subscriberList != null)
			subscriberList.remove(subscriber);
	}
	
	static class SubscriberObject {
		private final Object subscriber;
		private final Consumer<String> callback;
		
		public SubscriberObject(Object subscriber,
				Consumer<String> callback) {
			this.subscriber = subscriber;
			this.callback = callback;
		}

		public Object getSubscriber() {
			return subscriber;
		}

		public Consumer<String> getCallBack() {
			return callback;
		}

		@Override
		public int hashCode() {
			return subscriber.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			return subscriber.equals(obj);
		}
		
		
	}
}
