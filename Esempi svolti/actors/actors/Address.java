package it.unipd.pdp2021.actors;

/** An address is a reference to an actor, i.e. something a message can be sent to. */
public interface Address<T> {

  Address<T> tell(T msg);
}
