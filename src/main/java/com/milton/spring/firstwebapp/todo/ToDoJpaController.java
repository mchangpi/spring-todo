package com.milton.spring.firstwebapp.todo;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import jakarta.validation.Valid;

@Controller
@SessionAttributes("name")
public class ToDoJpaController {
  
  private Logger logger = LoggerFactory.getLogger(getClass());
  private ToDoRepository toDoRepository;
  
  @Autowired
  public ToDoJpaController(ToDoRepository toDoRepository) {
    super();
    this.toDoRepository = toDoRepository;
  }

  @RequestMapping(value={"", "/", "listtodos"})
  public String listAllToDos(ModelMap model){
    List<ToDo> toDos = toDoRepository.findByUsername(getLoggedInUsername());
    toDos.sort((t1, t2) -> t1.getId() - t2.getId());
    model.addAttribute("toDos", toDos);
    model.addAttribute("name", getLoggedInUsername());    
    
    logger.debug(getLoggedInUsername());
    toDos.forEach((todo) -> logger.debug(todo.toString()));
    return "listToDos";
  }

  @RequestMapping(value = "addtodo", method = RequestMethod.GET)
  public String newToDo(ModelMap model){
    ToDo toDo = new ToDo(0, getLoggedInUsername(), "", LocalDate.now().plusMonths(1), false);
    model.addAttribute("toDo", toDo); //2-way binding, <form:form method = "post" modelAttribute = "toDo">
    return "addToDo";
  }

  @RequestMapping(value = "addtodo", method = RequestMethod.POST)
  public String addToDo(@ModelAttribute @Valid ToDo toDo, BindingResult result) {
    if(result.hasErrors()) {
      logger.debug(result.toString());
      return "addToDo";
    }

    toDo.setUsername(getLoggedInUsername());
    toDoRepository.save(toDo);
    
    logger.debug(toDo.toString());
    return "redirect:listtodos";
  }

  @RequestMapping(value = "updatetodo", method = RequestMethod.GET)
  public String showUpdateToDo(@RequestParam int id, ModelMap model) {
    ToDo toDo = toDoRepository.findById(id).get(); 
    model.addAttribute("toDo", toDo);
    return "addToDo";
  }

  @RequestMapping(value = "updatetodo", method = RequestMethod.POST)
  public String updateToDo(@ModelAttribute @Valid ToDo toDo, BindingResult result) {
    if(result.hasErrors()) {
      logger.debug(result.toString());
      return "addToDo";
    }
    toDo.setUsername(getLoggedInUsername());
    toDoRepository.save(toDo);

    logger.debug(toDo.toString());
    return "redirect:listtodos";
  }

  @RequestMapping("deletetodo")
  public String deleteToDo(@RequestParam int id){
    toDoRepository.deleteById(id);
    return "redirect:listtodos";
  }

  private String getLoggedInUsername() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    return auth.getName();
  }
}
