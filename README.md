# Spring-5-Learning

**Spring 的2大核心思想: IOC/DI(控制反转) 以及 AOP(面向切面编程)**

[TOC]



# IOC/DI 

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

### Spring入门之普通方法创建对象

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

### Spring创建对象之通过工厂创建对象

我们可以通过工厂来创建对象

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

### Spring创建对象之静态工厂创建对象

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

## Spring之依赖注入(DI)

之前我们我们说过，类与类之间的依赖关系，可以直接在一个类中直接new一个类，那么这样的方式耦合很高。或者，我们可以通过构造方法或者set方法进行注入。Spring也为我们提供2中依赖注入:构造方法注入以及set方法注入。

### Spring依赖注入之构造

### 方法注入

**假设我们要给Person注入一些属性。**

```java
public class Person {
    private String name;
    private Integer age;

    public Person(String name, Integer age) {
        this.name = name;
        this.age = age;
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

1.首先在applicationContext.xml中创建一个bean 对象。这个和之前通过bean创建对象没有区别。

2.在bean标签中 我们需要写一个<constructor-arg name="" value=""/>

**constructor-arg标签中有几个重要的属性:**

**name:用于指定给构造方法中指定名称参数赋值。**

**Index:表示构造函数中参数的位置，从0开始**

**type:指定要注入的数据的数据类型**

**value:用于提供基本数据类型和string类型数据**

**ref:用于指定bean类型的数据，它指定的是spring的IOC 核心容器中出现过的bean对象**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd">
		<!--在Person类的构造方法中，传递的参数是String name 以及int age
 				因此我们可以通过 name属性来选择参数，value参数来赋值
				注:这里的value参数只能写基本数据类型或者String
		-->
    <bean id="person" class="com.java.domain.Person">
        <constructor-arg name="name" value="kevin"/>
        <constructor-arg name="age" value="20"/>
    </bean>
</beans>
```

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd">

  	<!--由于index是指参数的位置，从0开始，那么可读性比较差，因为我们不知道这个index
 				对应的数据类型是什么，因此我们可以用index+type 这两个属性一起使用。
				一般来说，使用 name+value这一组比较方便。
				注:如果是基本数据类型，type需要些包装类的全类名，否则会报异常。
		-->
    <bean id="person" class="com.java.domain.Person">
        <constructor-arg index="0" type="java.lang.String" value="kevin"/>
        <constructor-arg index="1" type="java.lang.Integer" value="20"/>
    </bean>
</beans>
```

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd">
		
  	<!--员工做班车去上班，员工依赖于班车
 				因此我们需要把班车这个对象注入到员工对象中。
				1.首先我们先配置ShuttleBus对象
				2.通过<constructor-arg>标签实现注入。
				这里我们使用ref 引用来注入ShuttleBus。
				ref引用就是bean的id值，找到对应的id值即可实现注入
		-->
    <bean id="vehicle" class="com.java.vehicle.ShuttleBus"/>
    <bean id="employee" class="com.java.vehicle.Employee">
        <constructor-arg name="vehicle" ref="vehicle"/>
    </bean>
</beans>
```

### Spring依赖注入之set方法注入:

通过set方法注入，我们需要用到<property>标签。该标签只有3个属性:

**name, ref, value**，意思和<constrcutor-arg>标签中的**name ref value**一样

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd">
		<!--如果想通过set方法注入，person类中得有一个空的构造方法-->
    <bean id="person" class="com.java.domain.Person">
        <property name="name" value="kevin"/>
        <property name="age" value="20"/>
    </bean>
</beans>
```

### Spring注入集合数据

一个类中会有集合数据: List, set, map, array等。通过set方法注入集合数据。

**注:在配置过程中, List set array的标签可以互用。**

​	 **map和prop的标签可以互用，因为map和prop都属于key-value形式**

```java
package com.java.domain;

import java.util.*;

public class Person {
    private String name;
    private Integer age;
    private String[] myArrays;
    private List<String> list;
    private Set<String> set;
    private Map<String,String> map;
    private Properties pro;

    public Person(){}
    public Person(String name, Integer age) {
        this.name = name;
        this.age = age;
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

    public void setMyArrays(String[] myArrays) {
        this.myArrays = myArrays;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public void setSet(Set<String> set) {
        this.set = set;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }

    public void setPro(Properties pro) {
        this.pro = pro;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", myArrays=" + Arrays.toString(myArrays) +
                ", list=" + list +
                ", set=" + set +
                ", map=" + map +
                ", pro=" + pro +
                '}';
    }
}
```

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="person" class="com.java.domain.Person">
        <property name="name" value="kevin"/>
        <property name="age" value="20"/>
        <property name="myArrays">
            <array>
                <value>A</value>
                <value>B</value>
                <value>C</value>
            </array>
        </property>
        <property name="list" >
            <list>
                <value>AA</value>
                <value>BB</value>
                <value>CC</value>
            </list>
        </property>
        <property name="set">
            <set>
                <value>A</value>
                <value>E</value>
            </set>
        </property>
        <property name="map">
            <map>
                <entry key="1" value="kevin"/>
                <entry key="2" value="Bob"/>
            </map>
        </property>
        <property name="pro">
            <props>
                <prop key="4">David</prop>
                <prop key="5">James</prop>
            </props>
        </property>
	</bean>
</beans>
```

## Spring中bean对象的研究

1.我们在xml配置文件中可以配置任何对象，那么这些对象会被存入到spring的IOC容器中。那么这些对象到底是什么时候被创建好了？

我们在Person类的构造方法中打印一句话:

```java
public class Person {
    private String name;
    private Integer age;
    private String[] myArrays;
    private List<String> list;
    private Set<String> set;
    private Map<String,String> map;
    private Properties pro;

    public Person(){
        System.out.println("Person对象被创建好了...");
    }
  	//set 和 get 方法....
```

我们想要获取Person对象:

1.先创建SpringIOC的容器 

```java
ApplicationContext ac=new ClassPathXmlApplicationContext("applicationContext.xml");
```

2.通过getBean方法来获取对象:

```java
Person person = ac.getBean("person", Person.class);
```

假设我们只创建SpringIOC容器，并不调用getBean方法，然后执行，我们发现在控制台中还是会输出Person对象被创建好了...。由此我们可以证明Spring是默认在创建IOC时候，就顺便把bean对象创建好并存进IOC容器。那么该对象的生命周期和IOC是相同的，只要容器在，那么该对象就会一直在。因此，默认情况下，bean对象是单例的。

**两次获取person对象，控制台中只会输出一次Person对象被创建好了...。 并且使用==号来判断是否同一个对象，结果为true。**

```java
public static void main(String[] args) {
    ApplicationContext ac=new ClassPathXmlApplicationContext("applicationContext.xml");
    Person person1 = ac.getBean("person", Person.class);
    Person person2=ac.getBean("person",Person.class);
    System.out.println(person1==person2);//true

}
```

我们可以通过bean标签中的scope属性来设置对象创建的时机。

Scope: 

1.singleton 单例

2.prototype 多例

如果不添加scope属性，那么默认是单例的。

```xml
<!--如果是多例的，那么在getBean方法获取对象时才创建 -->
<bean id="person" class="com.java.domain.Person" scope="protoype" >
    <property name="name" value="kevin"/>
    <property name="age" value="20"/>
</bean>
```

## Spring之注解

我们除了使用xml配置文件来创建对象和注入对象以外，我们还可以使用注解的方式实现创建对象和注入对象。

### @Component

@Component: 用于把当前类对象存入spring容器中。属性：value。用于指定bean的id，当然可以不写，默认是当前类名的且首字母小写。

1.因为要使用注解开发，那么我们需要从spring官网中找到注解的约束并粘贴到applicationContext.xml中

2.创建一个类，并在类上写@Component

3.在applicationConetxt.xml配置文件中添加component-scan标签，该标签可以告知spring在创建容器时，如果有@Component，那么就创建该对象并存入IOC容器中.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       https://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context
       https://www.springframework.org/schema/context/spring-context.xsd">
        
    <context:component-scan base-package="com.java"/>
    <context:annotation-config/>
    
</beans>
```

```java
ApplicationContext ac=new AnnotationConfigApplicationContext("com.java");
Person person = ac.getBean("person", Person.class);
```

4.通过new AnnotationConfigApplicationContext来创建容器，并通过getBean方法获取对象。

由于web 开发可以分成3层架构，web表现层，service业务逻辑层，dao持久层。

那么@Component可以细分为3中

@Controller它对应的是表现层

@Service它对应的是业务逻辑层

@Repository它对应的是dao层

@Controller @Service @Repository 这三个功能和@Component功能一模一样,只是因为web3层架构所以才会细分。

### @Autowired

该注解的作用是自动按照类型注入。该注解可以作用在变量，方法上。

我们知道Spring的IOC容器是HashMap，Key是String类型的，Value是Object类型的。

**如果容器中只存在唯一的类型，那么只要注入的数据类型和容器中的类型匹配就可以成功自动注入。**

```java
@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountDao accountDao;
}
```

| Key：String类    | value：Object类        |
| ---------------- | ---------------------- |
| "accountDao"     | AccountDaoImpl对象     |
| "accountService" | AccountServiceimpl对象 |
|                  |                        |

**如果容器中存在多个相同的类型，那么需要通过变量名来判断。如果找到则成功自动注入。**

```java
@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountDao accountDao1;
}
```

| Key: String类    | Value:Object类         |
| ---------------- | ---------------------- |
| "accountDao1"    | AccountDaoImpl对象     |
| "accountDao2"    | AccountDaoImpl对象     |
| "accountService" | AccountServiceimpl对象 |

### @Qualifier

**如果容器中存在多个相同的类型，但是变量名却不匹配，如下所示。accountDao变量名和key中的任何一个值不匹配，找不到对象。那么解决该方法就是@Autowired和@Qualifier一起使用**

**因此@Qualifier的作用就是在按照勒种注入的基础上，再按照名称注入，他在给类成员注入时必须和@Autowired一起使用。@Qualifier可以单独使用，但是只能作用在方法时，后续讲解使用注解来替换applicationContext.xml配置时再详细讲解。**

```java
@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountDao accountDao;
}
```

| Key: String类    | Value:Object类         |
| ---------------- | ---------------------- |
| "accountDao1"    | AccountDaoImpl对象     |
| "accountDao2"    | AccountDaoImpl对象     |
| "accountService" | AccountServiceimpl对象 |

```java
@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    @Qualifier(value = "accountDao1")
    private AccountDao accountDao;
}
```

```java
@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    @Qualifier(value = "accountDao1")
    private AccountDao accountDao;
}
```

**@Autowired @Qualifier 不能给String类型和基本数据类型注入。**

### @Value

作用: 用于注入基本数据类型和String类型数据

属性:value 赋值。 他可以使用Spring spEL表达式 --> ${表达式}

```java
@Component
public class Person {
    @Value("kevin")
    private String name;
  	//SPEL表达式
    @Value("${男}")
    private String sex;
    @Value("20")
    private int age;

    public Person(){
        System.out.println("Person对象被创建了");
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", age=" + age +
                '}';
    }
}
```

### @Scope

作用:用于指定bean的作用范围。

属性 value: singleton(单例)/prototype(多例)

和在xml配置文件中bean标签配置scope属性是一模一样。



## Spring的新注解

### @Configuration

该注解作用在类上，使用了该注解，表示该类是一个配置类。

### @ComponentScan

作用在类上，该注解的作用是在创建容器时要扫描的包。使用属性basepackages="" 来指定哪些包需要被扫描.

### @Bean

该注解的作用在方法上，把方法的返回值作为bean对象存入容器中。该注解有个属性name，用于指定bean的id。默认值是当前方法的名称。

### @Import

该注解作用在类上，表示导入副配置文件。一般的@Configuration配置一些公共的信息，其他的信息可以单独地创建一个类，并在主配置类中使用@Import导入即可。比如和数据库相关的信息，我们可以创建一个新的类专门配置数据库新，然后在主配置类中通过@Import注解导入配置数据库的类即可。

### @propertySource

用来加载指定的配置文件。该属性value="classpath:..." 表示加载类路径下的。

### 示例

利用新注解完成从数据库中获取所有账户信息。

**1.创建SpringConfiguration类，该类是一个配置类。**

```java
package com.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Configuration//表示该类是一个配置类
//表示com.java包下所有的类有@Service,@Repository,@Controller都会被加载进容器
@ComponentScan(basePackages = "com.java")
//加载类路径下的配置文件
@PropertySource("classpath:JdbcConfig.properties")
@Import(JdbcConfig.class)//导入副配置文件
public class SpringConfiguration {
}
```

**2.编写副配置文件(和数据库相关)**

```java
package com.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;


public class JdbcConfig {
    @Value("${jdbc.driver}")
    private String driver;

    @Value("${jdbc.url}")
    private String url;

    @Value("${jdbc.user}")
    private String user;

    @Value("${jdbc.password}")
    private String password;
	
  	//表示方法返回值会作为bean对象存入容器中，于此同时dataSource会注入到QueryRunner中
    @Bean("queryRunner")
    public QueryRunner createQueryRunner(@Qualifier("ds1") DataSource dataSource){
        return new QueryRunner(dataSource);
    }

	  //将DataSource作为bean对象存入容器。
    @Bean("ds1")
    public DataSource createDataSource1(){
        try{
            ComboPooledDataSource dataSource=new ComboPooledDataSource();
            dataSource.setDriverClass(driver);
            dataSource.setJdbcUrl(url);
            dataSource.setUser(user);
            dataSource.setPassword(password);
            return dataSource;
        }catch (Exception e){
            throw new RuntimeException();
        }
    }

}
```

**3.编写accountService接口**

```java
public interface AccountService {

    /**
     * 查询所有账户
     * @return
     */
    public List<Account> findAll();
}
```



**4.编写accountService实现类**

```java
@Service("accountService")
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountDao accountDao;
	
  	/**
     * 查询所有账户
     * @return
     */
    @Override
    public List<Account> findAll() {
        return accountDao.findAll();
    }
}
```



**5.编写accountDao接口**

```java
public interface AccountDao {

    /**
     * 查询所有账户
     * @return
     */
    public List<Account> findAll();
}
```



**6.编写accountDao实现类**

```java
@Repository("accountDao")
public class AccountDaoImpl implements AccountDao {
    @Autowired
    private QueryRunner runner;

    /**
     * 查询所有账户
     * @return
     */
    @Override
    public List<Account> findAll() {
        String sql="select * from account1";
        try {
            return runner.query(sql,new BeanListHandler<Account>(Account.class));
        } catch (Exception e) {
           throw new RuntimeException();
        }
    }
}
```



**7.编写单元测试**

```java
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringConfiguration.class)
@PropertySource("classpath:JdbcConfig.properties")
public class AccountTest {
    @Autowired
    private AccountService service;

    @Test
    public void testFindAll(){
        List<Account> accounts = service.findAll();
        for(Account account:accounts){
            System.out.println(account);
        }
    }
}
```

# spring之AOP

AOP:Aspect Oriented Programming的缩写,表示面向切面编程。可以理解为，在不修改源码的情况下，对已有的方法进行增强。Spring的AOP运用代理模式对方法进行增强。在编写代码中，我们常常会遇到有很多方法都需要写相同的代码，比如数据库事务，我们都需要编写开启事务，提交事务，事务回滚。因此我们可以提取这些代码通过 代理模式在不修改源码情况下对需要用到事务控制的方法进行增强。

## AOP相关的术语

**JoinPoint(连接点):** 指被拦截到的点，在这里是指被代理对象的方法。

**Pointcut(切入点):** 对哪些连接点进行拦截定义，也就哪些方法需要增强。

**Advice(通知):** 对拦截到的方法进行增强。有5种通知类型。前置通知，后置通知，异常通知，最终通知，环绕通知。

**前置通知**:在切入点方法之前执行

**后置通知**:在切入点方法正常执行之后再执行。

**异常通知:** 切入点方法出现异常后执行。

**最终通知:** 无论切入点方法是否正常执行，都在在其后执行。

**环绕通知:**将前置通知，后置通知，异常通知和最终通知融合到一起。

## 基于XML的AOP配置前置、后置、异常、最终通知

1.**创建一个Logger类，该类中有4个方法 before，afterReturning，afterThrowing,after 分别代表前置通知，后置通知，异常通知和最终通知**

```java
package com.java.utils;

public class Logger {

    public void before(){
        System.out.println("前置通知.....开启事务");
    }


    public void afterReturning(){
        System.out.println("后置通知......提交事务");
    }

    public void afterThrowing(){
        System.out.println("异常通知.......事务回滚");
    }

    public void after(){
        System.out.println("最终通知....释放资源");
    }


}
```



**创建一个AccountService接口以及实现类。里面有个update方法.** **update方法会被增强。**

```java
public interface AccountService {

    /**
     * 模拟查询所有账户
     */
    public void update();
}
```



```java
public class AccountServiceImpl implements AccountService {

    /**
     * 模拟查询所有账户
     */
    @Override
    public void update() {
        System.out.println("模拟更新账户");
    
    }
}
```



3**.在resources下创建applicationContext.xml并进行AOP配置。其中pointcut是指切入点，表示哪些方法需要被增强。在此模拟中，我们期望update方法被增强。**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/aop
        https://www.springframework.org/schema/aop/spring-aop.xsd">

    <!--配置accountService -->
    <bean id="accountService" class="com.java.service.impl.AccountServiceImpl"/>

    <!--配置logger通知 -->
    <bean id="logger" class="com.java.utils.Logger"/>

    <!-- spring中基于xml的aop配置步骤
         1.把通知的bean也交给spring来管理
         2.使用aop:config标签表明开始aop的配置
         3.使用aop:aspect标签表明开始配置切面
                    id属性:是给切面提供一个唯一标识
                    ref:是指定通知类bean的id
         4.在aop:aspect标签内部使用对应的标签来配置通知的类型
                aop:before:是前置通知的意思
                aop:after-returning:后置通知
                aop:after-throwing:异常通海
                aop:after:最终通知
                method:指定哪个方法对应哪个通知比如aop:before 对应before方法
                pointcut:用于指定切入点表达式，该表达式的含义指的是对业务层哪些方法增强
                        切入点表达式的写法：execution(表达式)。
                            表达式写法:访问修饰符 返回值 包名.类名.方法名(参数列表)
                            访问修饰符可以省略。
                            返回值可以使用通配符，表示任意返回值
                            包名可以使用通配符，表示任意包，但是有几级包，就需要写几个*.,如果四级包:*.*.*.*.
                                包名还可以使用*..表示当前包以及子包
                            类名的方法都可以使用*来实现统配
                            参数列表:
                                可以直接写数据类型
                                    基本类型直接写名称: int
                                    引用类型写包名.类名的方式: java.lang.String
                                可以使用通配符*表示任意类型，但是必须有参数
                                可以使用..表示有无参数均可，有参数可以是任意类型
                        全通配写法:
                            * *..*.*(..)

                        实际开发中切入点表达式通常写法:
                            切到业务层实现类下的所有方法
                            * com.java.service.impl.*.*(..)
    -->
    <!-- 配置aop-->
    <aop:config>
        <aop:aspect id="logAdvice" ref="logger">
            <aop:before method="before" pointcut="execution(* com.java.service.impl.*.*(..))"/>
            <aop:after-returning method="afterReturning" pointcut="execution(* com.java.service.impl.*.*(..))"/>
            <aop:after-throwing method="afterThrowing" pointcut="execution(* com.java.service.impl.*.*(..))"/>
            <aop:after method="after" pointcut="execution(* com.java.service.impl.*.*(..))"/>
        </aop:aspect>
    </aop:config>
</beans>
```

## 基于XML的AOP配置环绕通知

**1.配置logger类 只需要一个around方法。**

```java
package com.java.utils;

import org.aspectj.lang.ProceedingJoinPoint;

public class Logger {
    public Object around(ProceedingJoinPoint pjp){
        Object rtValue=null;
        try {
            System.out.println("前置通知.....");
            //获取方法执行所需的参数
            Object[] args = pjp.getArgs();
            //执行.在此之前是前置通知
            rtValue=pjp.proceed(args);
            System.out.println("后置通知......");
            //在此之后是后置通知
            //返回
            return rtValue;
        }catch (Throwable e){
            //异常通知
            System.out.println("异常通知.......");
            throw new RuntimeException(e);
        }finally {
            //最终通知
            System.out.println("最终通知");
        }
    }

}
```

2.**创建一个AccountService接口以及实现类。里面有个update方法.** **update方法会被增强。**

```java
public interface AccountService {

    /**
     * 模拟查询所有账户
     */
    public void findAll();
}
```



```java
public class AccountServiceImpl implements AccountService {

    /**
     * 模拟查询所有账户
     */
    @Override
    public void findAll() {
        System.out.println("模拟查询所有账户");
    }
}
```



3**.在resources下创建applicationContext.xml并进行AOP配置。其中pointcut是指切入点，表示哪些方法需要被增强。在此模拟中，我们期望update方法被增强。**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/aop
        https://www.springframework.org/schema/aop/spring-aop.xsd">

    <!--配置accountService -->
    <bean id="accountService" class="com.java.service.impl.AccountServiceImpl"/>

    <!--配置logger通知 -->
    <bean id="logger" class="com.java.utils.Logger"/>
 
    <!-- 配置aop-->
    <aop:config>
        <aop:aspect id="logAdvice" ref="logger">
            <aop:around method="around" pointcut="execution(* com.java.service.impl.*.*(..))"/>
        </aop:aspect>
    </aop:config>
</beans>
```

## spring中的事务控制

案例根据用户名进行转账。

**1.配置applicationContext.xml配置文件**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xmlns:tx="http://www.springframework.org/schema/tx" xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/tx
        https://www.springframework.org/schema/tx/spring-tx.xsd
        http://www.springframework.org/schema/aop
        https://www.springframework.org/schema/aop/spring-aop.xsd http://www.springframework.org/schema/context https://www.springframework.org/schema/context/spring-context.xsd">

    <!--开启注解扫描 -->
    <context:component-scan base-package="com"/>

    <bean id="template" class="org.springframework.jdbc.core.JdbcTemplate">
        <property name="dataSource" ref="dataSource"/>
    </bean>

    <!-- 配置数据库连接池-->
    <bean id="dataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
        <property name="driverClassName" value="com.mysql.cj.jdbc.Driver"></property>
        <property name="url" value="jdbc:mysql:///mybatis?serverTimezone=GMT"></property>
        <property name="username" value="root"></property>
        <property name="password" value="zs19940302"></property>
    </bean>
  
  	<!-- spring基于xml的声明事务控制的配置步骤
         1.配置事务管理器
         2.配置事务通知
                此时我们需要导入事务的约束
                使用tx:advice标签配置事务通知
                    属性:
                        id:给事务通知起一个唯一标志
                        transactionManager:给事务通知一个事务管理器引用
         3.配置aop中的通用切入点表达式
         4.建立事务通知和切入点表达式的关系
         5.配置事务的属性
                在事务通知tx:advice标签内部
    -->

    <!-- 配置事务管理器-->
    <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <!-- 注入数据库连接池-->
        <property name="dataSource" ref="dataSource"/>
    </bean>

    <!-- 配置事务通知-->
    <tx:advice id="txAdvice">
        <tx:attributes>
            <!-- 表示find开头的方法不需要事务 因为是查询-->
            <tx:method name="find*" read-only="true" propagation="SUPPORTS"/>
            <!-- 表示出了find开头以外其他的方法都需要事务的支持-->
            <tx:method name="*" read-only="false" propagation="REQUIRED"/>
        </tx:attributes>
    </tx:advice>

    <!--配置aop -->
    <aop:config>
        <aop:pointcut id="pt1" expression="execution(* com.service.impl.*.*(..))"/>
        <!--建立切入点表达式和事务通知的关系 -->
        <aop:advisor advice-ref="txAdvice" pointcut-ref="pt1"/>
    </aop:config>
</beans>
```



**2.创建一个JavaBean对象Account，用于当获取数据库账户信息时存到Account类中**

```java
package com.domain;

public class Account {
    private Integer id;
    private String name;
    private Float money;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getMoney() {
        return money;
    }

    public void setMoney(Float money) {
        this.money = money;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", money=" + money +
                '}';
    }
}
```



**3.创建AccountDao接口以及实现类**

```java
public interface AccountDao {

    /**
     * 根据用户名字查询账户
     * @param name
     * @return
     */
    public Account findAccountByName(String name);

    /**
     * 更新账户
     * @param account
     */
    public void updateAccount(Account account);
}
```



```java
@Repository("accountDao")
public class AccountDaoImpl implements AccountDao {
    @Autowired
    private JdbcTemplate template;

    /**
     * 根据用户名字查询账户
     * @param name
     * @return
     */
    @Override
    public Account findAccountByName(String name) {
        String sql="select * from account1 where name=?";
        return template.queryForObject(sql,new BeanPropertyRowMapper<Account>(Account.class),name);
    }

    /**
     * 更新账户
     * @param account
     */
    @Override
    public void updateAccount(Account account) {
        String sql="update account1 set money=? where name=?";
        template.update(sql,account.getMoney(),account.getName());
    }
}
```



**4.创建accountService接口以及实现类**

```java
public interface AccountService {

    /**
     * 转账
     * @param sourceName
     * @param targetName
     * @param money
     */
    public void transfer(String sourceName,String targetName,Float money);

}
```



```java
@Service("accountService")
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountDao accountDao;


    @Override
    public void transfer(String sourceName, String targetName, Float money) {
        //1.获取转账人的账户信息
        Account source = accountDao.findAccountByName(sourceName);
        //2.获取收款人的账户信息
        Account target = accountDao.findAccountByName(targetName);
        //3.转账人账户减钱
        source.setMoney(source.getMoney()-money);
        //4.收款人账户加钱
        target.setMoney(target.getMoney()+money);
        //5.更新转账人的账户信息
        accountDao.updateAccount(source);

        //6.更新收款人的账户信息
        accountDao.updateAccount(target);
    }
}
```



**5.测试**

**如果在转账方法中最后更新2个账户之间添加一个异常，那么转账会失效，因为出现异常后会触发事务回滚。**

```java
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class AccountTest {
    @Autowired
    private AccountService service;

    @Test
    public void transfer(){
        service.transfer("kevin","jack",100f);
    }
}
```



