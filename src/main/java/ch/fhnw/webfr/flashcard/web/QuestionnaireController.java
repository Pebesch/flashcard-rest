package ch.fhnw.webfr.flashcard.web;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.fhnw.webfr.flashcard.domain.Questionnaire;
import ch.fhnw.webfr.flashcard.persistence.QuestionnaireRepository;

@RestController
@RequestMapping("/questionnaires")
public class QuestionnaireController {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private QuestionnaireRepository questionnaireRepository;

    @GetMapping
    public ResponseEntity<List<Questionnaire>> findAll() {
        return new ResponseEntity<List<Questionnaire>>(questionnaireRepository.findAll(), HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Questionnaire> findById(@PathVariable String id) {
        if(questionnaireRepository.existsById(id)) {
            return new ResponseEntity<Questionnaire>(questionnaireRepository.findById(id).get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping()
    public ResponseEntity<Questionnaire> create(@Valid @RequestBody Questionnaire questionnaire, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } else {
            return new ResponseEntity<Questionnaire>(questionnaireRepository.save(questionnaire), HttpStatus.CREATED);
        }
    }
}