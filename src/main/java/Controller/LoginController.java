package Controller;

import Model.LogEntity;
import Model.StudentEntity;
import Session.UserSession;
import Util.NewWindow;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javax.persistence.*;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;


public class LoginController {

    @FXML
    private TextField loginTextField;

    @FXML
    private TextField passwordTextField;


    private String login;
    private String password;

    private static EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory("default");

    public void login(ActionEvent event) throws IOException {
        login = loginTextField.getText();
        password = passwordTextField.getText();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Подтверждение");
        alert.setHeaderText("Подтверждение входа в систему");
        alert.setContentText("Войти в систему?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {

            if (getUserType(login) == 1 && password.equals(getUserPassword(login))) {
                EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
                UserSession.getInstance(getStudent(em, login).getSingleResult());
                em.close();
                ENTITY_MANAGER_FACTORY.close();
                NewWindow.setNewWindow("adminMenu.fxml", event);
            } else if (getUserType(login) == 2 && password.equals(getUserPassword(login))) {
                addLoginLog(login);
                ENTITY_MANAGER_FACTORY.close();
                NewWindow.setNewWindow("managerMenu.fxml", event);
            } else if (getUserType(login) == 3 && password.equals(getUserPassword(login))) {
                addLoginLog(login);
                ENTITY_MANAGER_FACTORY.close();
                NewWindow.setNewWindow("workerMenu.fxml", event);
            } else {
                Alert alert2 = new Alert(Alert.AlertType.ERROR);
                alert2.setTitle("Ошибка");
                alert2.setHeaderText("Ошибка авторизации");
                alert2.setContentText("Введенны неверные данные для авторизации");
                alert2.showAndWait();
            }
        }
    }

    private int getUserType(String login) {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        String query = "SELECT w.usertype FROM StudentEntity w WHERE w.login=:login";
        Query q = em.createQuery(query);
        q.setParameter("login", login);
        int result = 0;
        try {
            result = (int) q.getSingleResult();
        } catch (NoResultException e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return result;
    }

    private String getUserPassword(String login) {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        String query = "SELECT w.password FROM StudentEntity w WHERE w.login=:login";
        Query q = em.createQuery(query);
        q.setParameter("login", login);
        String result = null;
        try {
            result = (String) q.getSingleResult();
        } catch (NoResultException e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return result;
    }

    private void addLoginLog(String login) {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction et = null;
        TypedQuery<StudentEntity> tq = getStudent(em, login);
        tq.setParameter("login", login);
        try {
            et = em.getTransaction();
            et.begin();
            LogEntity log = new LogEntity();
            log.setTime(Timestamp.valueOf(LocalDateTime.now()));
            log.setActivity("Вошёл в систему");
            log.setStudentByIdStudent(tq.getSingleResult());
            UserSession.getInstance(tq.getSingleResult());
            em.persist(log);
            et.commit();
        } catch (Exception e) {
            if (et != null) {
                et.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    private TypedQuery<StudentEntity> getStudent(EntityManager em, String login) {
        return em.createQuery("SELECT w FROM StudentEntity w WHERE w.login=:login", StudentEntity.class).setParameter("login", login);
    }
}