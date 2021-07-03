import com.java.domain.Person;
import com.java.vehicle.Employee;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringTest {


    public static void main(String[] args) {
        ApplicationContext ac=new ClassPathXmlApplicationContext("applicationContext.xml");
        Person person1 = ac.getBean("person", Person.class);
        Person person2=ac.getBean("person",Person.class);
        System.out.println(person1==person2);//true
        //System.out.println(person);

    }
}
