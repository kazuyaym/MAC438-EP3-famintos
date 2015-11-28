JCC = javac

default: Main.class Monitor.class Philosopher.class

Main.class: Main.java
		$(JCC) Main.java

Monitor.class: Monitor.java
		$(JCC) Monitor.java

Philosopher.class: Philosopher.java
		$(JCC) Philosopher.java

clean: 
		$(RM) *.class *~
