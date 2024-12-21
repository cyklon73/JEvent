![[Java CI]](https://github.com/cyklon73/JEvent/actions/workflows/check.yml/badge.svg)
![[Latest Version]](https://maven.cyklon.dev/api/badge/latest/releases/de/cyklon/JEvent?prefix=v&name=Latest%20Version&color=0374b5)

# JEvent

JEvent provides a powerful and lightweight event system based on the syntax of the [Spigot Event System](https://www.spigotmc.org/wiki/using-the-event-api/).

# Usage

```java
public class MyEvent extends Event {
	private String name;
	
	public MyEvent(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}

public class MyListener {

	@EventHandler //marks the Method as EventHandler method
	//the parameter is the Event the handler should listen to
	public void onEvent(MyEvent event) {
		System.out.println("Event name: " + event.getName());
		//change the event data
		event.setName("Edited " + event.getName());
	}

}

public class Main {
	public static void main(String[] args) {
		//get the default Event manager
		EventManager manager = JEvent.getDefaultManager();

		//register the listener
		manager.registerListener(MyListener.class);
		//or
		manager.registerListener(new MyListener());

		//initialize Event
		MyEvent event = new MyEvent("MyEventName");
		
		System.out.println(event.getName()); //"MyEventName"
		
		//call Event
		manager.callEvent(event);
		//or
		event.callEvent(); //only possible on the default manager

		System.out.println(event.getName()); //"Edited MyEventName"
	}
}
```
# Installation

JEvent is hosted on a custom repository at [https://maven.cyklon.dev](https://maven.cyklon.dev/#/releases/de/cyklon/JEvent). Replace VERSION with the lastest version (without the `v` prefix).
Alternatively, you can download the artifacts from jitpack (not recommended).

### Gradle

```groovy
repositories {
  maven { url "https://maven.cyklon.dev/releases" }
}

dependencies {
  implementation "de.cyklon:JEvent:VERSION"
}
```

### Maven

```xml
<repositories>
  <repository>
    <id>cyklon</id>
    <url>https://maven.cyklon.dev/releases</url>
  </repository>
</repositories>

<dependencies>
  <dependency>
    <groupId>de.cyklon</groupId>
    <artifactId>JEvent</artifactId>
    <version>VERSION</version>
  </dependency>
</dependencies>
```
