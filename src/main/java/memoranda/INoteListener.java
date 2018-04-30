package main.java.memoranda;

public interface INoteListener {
  void noteChange(INote note, boolean toSaveCurrentNote);
}
