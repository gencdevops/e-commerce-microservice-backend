# Fmss e-commerce template

<br>

Bu projede, kullanıcıların kaydolabileceği, şifre değiştirebileceği, sepete ürün ekleyebileceği, sepetteki ürün sayısını düzenleyebileceği ve sipariş verip ödeme yapabileceği bir e-ticaret uygulaması geliştirilmiştir.

<br>

## Tech Stack
    Spring Boot
    Open Feign
    Eureka Server/Client
    Spring Data JPA
    Open API
    Lombok
    Mapstruct
    Redis
    Kafka
    ELK Logstash
    ElasticSearch
    MongoDB
    PostgreSQL
    Prometheus
    Grafana
    Kubernetes
    H2 Database
    Spring Config Server
    Zipkin
    Gradle
    JWT
    OpenShift
    Jenkins
    React
    

<br>

## Servisler ve İşlevleri

### User Servis
<ul>
<li>Kullanıcıların sisteme kayıt olmasını sağlar.</li>
<li>Kullanıcıların şifre değiştirme işlemini gerçekleştirir.</li>
</ul>
Kullanıcı bilgilerini girip kayıt olur (isim, soyisim, email, kullanıcı adı, parola). Daha sonra email ve şifresini girerek sisteme login olur. Sistemden JWT token alır ve LDAP'ta valide olur

### Basket Servis
<ul>
<li>Sepete ürün ekleme işlemini gerçekleştirir.</li>
<li>Sepetteki ürün sayısını artırma veya azaltma işlemini gerçekleştirir.</li>
<li>Sepeti tamamen boşaltma işlemini gerçekleştirir.</li>
</ul>

### Order Servis
<ul>
<li>Sipariş işlemlerini yönetir ve gerçekleştirir. Orderın üzerinde placeOrder methodu bulunmaktadır. placeOrder methodu payment'tan önce postgre database'e order ve orderoutbox diye iki kayıt atmaktadır. Order'ın statusu paymenttan önce RECEIVED'a çekilmektedir. Ödeme ve order adımları transactional bir method içerisinde gerçekleşmektedir. Eğer ödeme alınır ve orderın statusu preparinge çekilemezse, kafka içerisinde preparinge çekilir. Order service içerisinde schedular job çalışmaktadır. Atıl kalan orderoutbox kayıtlarını temizler. Eğer kafka da çökerse slack üzerinden alert yazısı gönderilir.</li>
</ul>

### Payment Servis
<ul>
<li>Ödeme işlemlerini gerçekleştirir ve yönetir.</li>
</ul>

### Product Servis
<ul>
<li>Ürünleri ID ile getirme işlemini gerçekleştirir.</li>
<li>Tüm ürünleri getirme işlemini gerçekleştirir.</li>
</ul>

## Uygulamadan Görüntüler
Monitoring tool
<br>
<b>Grafana</b>
<br>
![Grafana](https://user-images.githubusercontent.com/98235097/231936997-a5c67559-14f3-45c2-b12b-8a6f4b6873dd.gif)
<br>
<br>
Elastic Logstash Kibana uygulamanın içerisinde embedded logback xmle' bağlı kafka appender bulunmaktadır. Uygulamadaki loglar kafka appender üzerinden asenkron olarak hem mongo databaseine hem de file systeme gönderilmektedir. Zipkin aracılığıyla uygulamaya gelen her http thread'i trace edilmektedir.
<br>
<br>
<b>ELK</b>
![ELK](https://user-images.githubusercontent.com/52275789/231941484-fe8d6b41-db59-41c4-b3bf-72502b35dbe6.png)

<br>
<br>
<br>
<br>Uygulama uzerindeki tum resimler Aws S3 sunucudan alinip, getAllProduct methodu redis cache'e atmaktadir.
<br>
<img width="1005" alt="image" src="https://user-images.githubusercontent.com/98235097/231942164-2a1f4c10-5f3a-464c-8628-13fa380d7524.png">


## Bağlantılar
https://github.com/fmssbilisimtech/ecommerce-template-backend
<br>
https://github.com/gencdevops/e-commerce-microservice-backend
<br>
https://github.com/gencdevops/e-commerce-microservice-frontend
