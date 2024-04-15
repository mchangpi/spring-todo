package com.milton.spring.firstwebapp.todo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import jakarta.validation.Valid;

//@Service
public class ToDoService {
  
  private Logger logger = LoggerFactory.getLogger(getClass());
  private static List<ToDo> toDos = new ArrayList<ToDo>();
  
  private static int toDosCount = 0; 
  
  static {
    toDos.add(new ToDo(++toDosCount, "milton", "Learn AWS", LocalDate.now().plusYears(1), false));
    toDos.add(new ToDo(++toDosCount, "milton", "Learn DevOps", LocalDate.now().plusYears(2), false));
    toDos.add(new ToDo(++toDosCount, "milton", "Learn DDD", LocalDate.now().plusYears(3), false));
  }

  public List<ToDo> findByUsername(String username){
    Predicate<? super ToDo> checkToDoUserName = todo -> todo.getUsername().equalsIgnoreCase(username);
    return toDos.stream().filter(checkToDoUserName).toList();
  }
  
  public void addToDo(String username, String description, LocalDate targetDate, boolean done) {
    ToDo toDo = new ToDo(++toDosCount, username, description, targetDate, done);
    toDos.add(toDo);
  }
  
  public ToDo findById(int id) {
    Predicate<? super ToDo> checkToDoId = todo -> todo.getId() == id;
    return toDos.stream().filter(checkToDoId).findFirst().get();
  }
  
  public void updateToDo(@Valid ToDo toDo) {
    logger.debug("index " + toDo.getId());
    for(int i = 0; i < toDos.size(); i++){
      if(toDos.get(i).getId() == toDo.getId()) {
        toDos.set(i, toDo);
        break;
      }
    }
  }
  
  public void deleteById(int id) {
    Predicate<? super ToDo> checkToDoId = todo -> todo.getId() == id;
    toDos.removeIf(checkToDoId);
  }
}
