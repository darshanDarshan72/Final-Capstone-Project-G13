package com.doconnect.doconnectservice.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.doconnect.doconnectservice.dto.AnswerDTO;
import com.doconnect.doconnectservice.dto.UserDTO;
import com.doconnect.doconnectservice.entity.Answer;
import com.doconnect.doconnectservice.entity.Question;
import com.doconnect.doconnectservice.entity.Role;
import com.doconnect.doconnectservice.entity.User;
import com.doconnect.doconnectservice.enums.ERoles;
import com.doconnect.doconnectservice.repository.AnswerRepository;
import com.doconnect.doconnectservice.repository.QuestionRepository;
import com.doconnect.doconnectservice.repository.UserRepository;

  /*
     * @Author : Samarthan Reddy
     * Created Date : 25-8-2022
     * Modified Date : 28-8-2022
     * Description : Implementation of Answer Service
     * Params : None
     * Return Type : None
  */

@Service
public class AnswerServiceImpl implements AnswerService {

  @Autowired
  UserRepository userRepository;

  @Autowired
  QuestionRepository questionRepository;

  @Autowired
  AnswerRepository answerRepository;

  @Autowired
  EmailSenderService emailSenderService;

  /*
     * @Author : Samarthan Reddy
     * Created Date : 25-8-2022
     * Modified Date : 28-8-2022
     * Description : created method for adding of answer
     * Params : answerDTO
     * Return Type : String
   */

  public String addAnswer(@Valid AnswerDTO answerDTO) {
    answerRepository.save(this.mapDtoToAnswer(answerDTO));

    String body = "New Answer is added, Please Review the Answer\n" +
        "Question ID : " + answerDTO.getQuestion_id() + "\n" +
        "Answer: " + answerDTO.getAnswer();

    String subject = "Answer Added";

    List<UserDTO> adminList = this.getAllAdmins();
    adminList.forEach(admin -> {
      this.emailSenderService.sendMail(admin.getEmail(), body, subject);
    });

    return "Answer Added Successfully";
  }

  /*
     * @Author : Samarthan Reddy
     * Created Date : 25-8-2022
     * Modified Date : 28-8-2022
     * Description : Created method for getting all answers
     * Params : None
     * Return Type : List<AnswerDTO>
   */

  public List<AnswerDTO> getAllAnswers() {
    List<Answer> answerList = this.answerRepository.findAll();
    return answerList.stream().map(this::mapAnswerToDto).collect(Collectors.toList());
  }

/*
     * @Author : Samarthan Reddy
     * Created Date : 25-8-2022
     * Modified Date : 28-8-2022
     * Description : Getting all answers of questions
     * Params : question_id
     * Return Type : List<AnswerDTO>
   */

  public List<AnswerDTO> getAllAnswersOfQuestion(Long question_id) {
    Question question = this.questionRepository.findById(question_id)
        .orElseThrow(() -> new RuntimeException("Error : Question is not find"));
    List<Answer> answerList = this.answerRepository.findAllByQuestion(question)
        .orElseThrow(() -> new RuntimeException("Error : Answers for question not found"));
    return answerList.stream().map(this::mapAnswerToDto).collect(Collectors.toList());
  }

  /*
     * @Author : Samarthan Reddy
     * Created Date : 25-8-2022
     * Modified Date : 28-8-2022
     * Description : Getting of answers
     * Params : answer_id
     * Return Type : AnswerDTO
   */

  public AnswerDTO getAnswer(Long answer_id) {
    Answer answer = this.answerRepository.findById(answer_id)
        .orElseThrow(() -> new RuntimeException("Error: Answer is not found."));

    return mapAnswerToDto(answer);
  }

  /*
     * @Author : Samarthan Reddy
     * Created Date : 25-8-2022
     * Modified Date : 28-8-2022
     * Description : Getting all answers of user
     * Params : user_id
     * Return Type : answerList
   */

  public List<AnswerDTO> getAllAnswersOfUser(Long user_id)
    {
        User user = this.userRepository.findById(user_id).orElseThrow(() -> new RuntimeException("Error : User is not found"));
        List<Answer> answerList = answerRepository.findAllByUser(user).orElseThrow(() -> new RuntimeException("Error: Answer is not found."));
        return answerList.stream().map(this::mapAnswerToDto).collect(Collectors.toList());
    }


  /*
     * @Author : Samarthan Reddy
     * Created Date : 25-8-2022
     * Modified Date : 28-8-2022
     * Description : Created method for approving the answer
     * Params : answer_id
     * Return Type : String
   */

  public String approveAnswer(Long answer_id) {
    Answer answer = this.answerRepository.findById(answer_id)
        .orElseThrow(() -> new RuntimeException("Error: Answer is not found."));
    answer.setApproved(true);
    this.answerRepository.save(answer);
    return "Answer Approved";
  }

   /*
     * @Author : Samarthan Reddy
     * Created Date : 25-8-2022
     * Modified Date : 28-8-2022
     * Description : Created method for getting all approved answers
     * Params : None
     * Return Type : List<AnswerDTO>
   */

  public List<AnswerDTO> getAllApprovedAnswer() {

    List<Answer> answerList = this.answerRepository.findAllByIsApproved(true).orElseThrow(() -> new RuntimeException("Error: Answer is not found."));
    return answerList.stream().map(this::mapAnswerToDto).collect(Collectors.toList());

  }

   /*
     * @Author : Samarthan Reddy
     * Created Date : 25-8-2022
     * Modified Date : 28-8-2022
     * Description : Created method for mapping Dto to answer
     * Params : answerDTO
     * Return Type : answer
   */

  private Answer mapDtoToAnswer(AnswerDTO answerDTO) {
    Answer answer = new Answer();
    User user = userRepository.findById(answerDTO.getUser_id())
        .orElseThrow(() -> new RuntimeException("Error : User Not Found"));

    Question question = questionRepository.findById(answerDTO.getQuestion_id())
        .orElseThrow(() -> new RuntimeException("Error : Question is not find"));

    answer.setAnswer(answerDTO.getAnswer());
    answer.setUser(user);
    answer.setQuestion(question);
    answer.setApproved(answerDTO.isApprove());

    return answer;
  }

  /*
     * @Author : Samarthan Reddy
     * Created Date : 25-8-2022
     * Modified Date : 28-8-2022
     * Description : Created method for mapping answer to Dto
     * Params : answer
     * Return Type : AnswerDTO
   */

  private AnswerDTO mapAnswerToDto(Answer answer) {

    AnswerDTO answerDTO = new AnswerDTO();
    answerDTO.setAnswer_id(answer.getAnswer_id());
    answerDTO.setAnswer(answer.getAnswer());
    answerDTO.setUsername(answer.getUser().getUsername());
    answerDTO.setQuestion_id(answer.getQuestion().getQuestion_id());
    answerDTO.setUser_id(answer.getUser().getUser_id());
    answerDTO.setApprove(answer.isApproved());

    return answerDTO;

  }

  /*
     * @Author : Samarthan Reddy
     * Created Date : 25-8-2022
     * Modified Date : 28-8-2022
     * Description : Created method for deleting answer
     * Params : answer_id
     * Return Type : String
   */

  public String deleteAnswer(Long answer_id) {
    Answer answer = answerRepository.findById(answer_id)
        .orElseThrow(() -> new RuntimeException("Error: Answer is not found."));
    answer.setUser(null);
    answer.setQuestion(null);
    answerRepository.deleteById(answer_id);
    return "Answer deleted succesfully";

  }
    /*
     * @Author : Samarthan Reddy
     * Created Date : 25-8-2022
     * Modified Date : 28-8-2022
     * Description : Created method for getting all admins 
     * Params : None
     * Return Type : List<UserDTO>
   */

  public List<UserDTO> getAllAdmins() {
    List<User> userList = this.userRepository.findAll();

    List<UserDTO> adminList = new ArrayList<>();

    userList.forEach(user -> {
      Set<Role> roles = user.getRoles();
      roles.forEach(role -> {
        ERoles r = role.getRole();
        if (r == ERoles.ROLE_ADMIN) {
          adminList.add(this.mapUserToDto(user));
        }
      });

    });

    return adminList;
  }

  /*
     * @Author : Samarthan Reddy
     * Created Date : 25-8-2022
     * Modified Date : 28-8-2022
     * Description : Created method for mapping user to DTO
     * Params : user
     * Return Type : UserDTO
   */

  private UserDTO mapUserToDto(User user) {
    UserDTO userDTO = new UserDTO();

    userDTO.setUser_id(user.getUser_id());
    userDTO.setFirstName(user.getFirstName());
    userDTO.setLastName(user.getLastName());
    userDTO.setUsername(user.getUsername());
    userDTO.setEmail(user.getEmail());
    userDTO.setPhone(user.getPhone());
    userDTO.setPassword(user.getPassword());

    Set<String> userRoles = new HashSet<>();

    Set<Role> userSet = user.getRoles();
    userSet.forEach(role -> {
      ERoles r = role.getRole();
      if (r == ERoles.ROLE_ADMIN) {
        userRoles.add("ROLE_ADMIN");
      } else {
        userRoles.add("ROLE_USER");
      }
    });

    userDTO.setRoles(userRoles);

    return userDTO;

  }

}
