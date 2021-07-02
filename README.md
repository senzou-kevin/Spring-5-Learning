# Spring-5-Learning

**Spring 的2大核心思想: IOC/DI(控制反转) 以及 AOP(面向切面编程)**

## IOC/DI 

在传统的编程当中,如果一个类A 依赖于另一个类B， 我们可以在A类中new一个B类。但是这样做有个坏处，类和类之间的耦合度太高，不利于扩展以及维护。不符合高内聚低耦合的设计思想。下面用一个小案例进行解释:

假设一位公司员工每天开车去上班，以下是耦合度很高的情况。

```java
/**
 * 一位公司员工每天开车去上班
 */
public class Person {

    private Car car=new Car();
    
    public void goToWork(){
        car.take();
    }

}
```

```java
public class Car {

    public void take(){
        System.out.println("开车去上班。");
    }
}
```

如果需求改变，公司员工做班车去上班，我们需要做以下修改:

```java
public class Bus {
    
    public void take(){
        System.out.println("坐班车去上班");
    }
}
```

```java
/**
 * 一位公司员工每天开车去上班
 */
public class Person {
		//这里修改成ShuttleBus
    private ShuttleBus bus=new ShuttleBus();

    public void goToWork(){
      	//修改成bus调用take方法
        bus.take();
    }

}
```

如果每次都是这么修改,在现实开发中类和类分复杂关系远比案例中的类和类之间关系复杂,这样就造成不利于维护以及扩展。如果Person类依赖的不是实体类，而是抽象，并且通过构造方法或者set方法传递引用，这样可以减少类和类之间的耦合。代码如下

```java
/**
 * 交通工具接口
 */
public interface Vehicle {
    
    public void take();
}
```

```java
/**
 * 实现Vehicle接口并实现take方法
 */
public class Car implements Vehicle {

    public void take(){
        System.out.println("开车去上班。");
    }
}
```

```java
/**
 * 实现Vehicle接口，并实现take方法
 */
public class ShuttleBus implements Vehicle {

    public void take(){
        System.out.println("坐班车去上班");
    }
}
```

```java
/**
 * 一位公司员工每天开车去上班
 */
public class Person {

    private Vehicle vehicle;
    
    public Person(){}
    public Person(Vehicle vehicle){
        this.vehicle=vehicle;
    }

    public void goToWork(){
        vehicle.take();
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }
}
```

相对于原来的代码，这样的设计可以减少代码修改, 我们只需要把Vehicle实现类创建好并通过构造方法或者set方法传递即可。

```java
public class Test {

    public static void main(String[] args) {
        //创建一个ShuttleBus对象
        ShuttleBus bus=new ShuttleBus();
        //创建一个Person对象
        Person person=new Person();
        //也可以通过构造方法传递
        //Person person=new Person(bus);
        person.setVehicle(bus);
        person.goToWork();
    }
}
```

这样方式虽然耦合度相比原来的代码确实降低了，因为Person类中的代码无需再修改。可是这样就出现了新的问题，在main方法中，我们还是使用了new关键字进行创建对象。然后把bus对象通过构造方法或者set方法传入到Person类中。

解决方法:

​	**Spring通过IOC(控制反转)/DI(依赖注入) 帮我们解决此问题。我们知道，创建对象方式不止new关键字这一种，我们还可以通过反射技术创建对象。Spring底层通过反射的技术帮我们创建对象，并且存入到容器中。该容器是HashMap。对于开发者来说，只需要在xml配置文件中配置一些对象信息即可。除了创建对象以外，spring也可以实现依赖注入: 在上面的例子中,人和交通工具通过构造方法或者set方法实现依赖关系。**



## Spring入门之创建对象

1.创建一个maven工程

2.在pom.xml文件中导入一些需要的jar包

```xml
<dependency>
  <groupId>org.springframework</groupId>
  <artifactId>spring-context</artifactId>
  <version>5.2.9.RELEASE</version>
</dependency>
<dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>4.12</version>
      <scope>test</scope>
</dependency>
```

3.创建一个Person类

```java
public class Person {
    private String name;
    private Integer age;

    public Person(){}
    public Person(String name,Integer age){
        this.name=name;
        this.age=age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
```

4.在resources资源包下创建一个xml配置文件，xml配置文件名字可以自己取。这里我会取名:

applicationContext.xml

5.往applicationContext.xml里导入spring的约束，可以从spring官网中查找:

https://docs.spring.io/spring-framework/docs/current/reference/html/core.html

6.在xml配置文件中，我们可以将需要用到的对象写在该配置文件中。目前就一个Person对象。

在该配置文件中，我们需要一个bean标签用来配置对象信息，其中有2个重要的属性。

id: 给该对象(bean)取一个名字，后续可以通过id来获取对象

class:全类名

**有了这两个关键属性后，当spring解析xml配置文件时，会把id作为key存入map中，由于有了全类名，spring可以使用反射技术创建对象并存入到key对应的value中。**

```java
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="person" class="com.java.domain.Person"/>
</beans>
```

注:目前仅仅是通过spring帮我们创建对象。所以约束仅仅需要这些就足够。如果后续使用spring的其他功能，那么则需要相应的约束。

7.创建一个Test类，使用spring创建对象。

```java
public class TestSpring {

    public static void main(String[] args) {
        //1.实例化一个ApplicationContext,在创建applicationContext实例对象时，我们需要把
      	//配置文件的名称通过构造方法形式传递。
        ApplicationContext ac=new ClassPathXmlApplicationContext("applicationContext.xml");
      	/**
      	两种方式获取对象：
      		1.只需要bean 的id 返回的是object对象，需要转型
      		2.bean的id+该类的class类 这样返回对象不需要转型
      	**/
        //Person person =(Person)ac.getBean("person");
        Person person=ac.getBean("person",Person.class);
    }
}
```

## Spring创建对象之通过工厂创建对象

我们可以通过示例工厂来创建对象

1.创建一个Aniaml类

```java
public class Animal {
    private String animalName;

    public Animal(){}
    public Animal(String animalName){
        this.animalName=animalName;
    }

    public String getAnimalName() {
        return animalName;
    }

    public void setAnimalName(String animalName) {
        this.animalName = animalName;
    }
}
```

2.创建一个BeanFactory

```java
public class BeanFactory {


    public Animal getAnimal(){
        Animal animal=new Animal();
        animal.setAnimalName("dog");
        return animal;
    }
}
```

3.配置xml

因为BeanFactory也是一个对象，因此先配置好BeanFacotory对象

因为Animal是通过工厂来创建的，那么我们需要用到factory-bean以及factory-method属性。

factory-bean:传入一个刚刚配置好的beanFactory即可

factory-method:将工厂方法传入，我们这里是getAnimal()方法

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd">
    
    <bean id="beanFactory" class="com.java.beanfactory.BeanFactory"/>
    <bean id="animal" factory-bean="beanFactory" factory-method="getAnimal"/>
</beans>
```

## Spring创建对象之静态工厂创建对象

该方法和工厂方法的区别就在于不需要创建工厂对象。

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd">
    <bean id="animal02" class="com.java.beanfactory.BeanStaticFactory" factory-method="getAnimal"/>
</beans>
```

