package com.doconnect.doconnectservice.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.doconnect.doconnectservice.dto.AnswerDTO;
import com.doconnect.doconnectservice.services.AnswerServiceImpl;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/doconnect/answer")

/*
 * @Author : Samarthan Reddy
 * Created Date : 25-08-2022
 * Modified Date : 28-08-2022
 * Description : Created answer controller for CRUD mappings
 * Params : None
 * Return Type : None
 */

public class AnswerController {

   @Autowired
   AnswerServiceImpl answerService;

   @GetMapping("/getall")
   public ResponseEntity<List<AnswerDTO>> getAllAnswers() {
      return ResponseEntity.ok(this.answerService.getAllAnswers());
   }

   @GetMapping("/approve/{answer_id}")
    public ResponseEntity<String> approveAnswer(@PathVariable Long answer_id)
    {
        return ResponseEntity.ok(answerService.approveAnswer(answer_id));
    }

   @GetMapping("/getAllApprovedAnswers")
   public ResponseEntity<List<AnswerDTO>> getAllApprovedAnswers()
   {
      return ResponseEntity.ok(answerService.getAllApprovedAnswer());
   }

   @GetMapping("/{answer_id}")
   public ResponseEntity<AnswerDTO> getAnswer(@PathVariable Long answer_id)
   {
      return ResponseEntity.ok(this.answerService.getAnswer(answer_id));
   }

   @GetMapping("/question/{question_id}")
   public ResponseEntity<List<AnswerDTO>> getAllAnswersOfQuestion(@PathVariable Long question_id)
   {
      return ResponseEntity.ok(this.answerService.getAllAnswersOfQuestion(question_id));
   }

   @GetMapping("/user/{user_id}")
    public ResponseEntity<List<AnswerDTO>> getAllAnswersOfUser(@PathVariable Long user_id)
    {
        return ResponseEntity.ok(answerService.getAllAnswersOfUser(user_id));
    }

   @PostMapping("/addanswer")
   public ResponseEntity<String> addAnswer(@Valid @RequestBody AnswerDTO answerDTO) {
      return ResponseEntity.ok(this.answerService.addAnswer(answerDTO));
   }

   @DeleteMapping("/{answer_id}")
   public ResponseEntity<String> deleteAnswer(@PathVariable Long answer_id) {
      return ResponseEntity.ok(answerService.deleteAnswer(answer_id));
   }

}
