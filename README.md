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

<img width="637" alt="image" src="https://user-images.githubusercontent.com/98235097/231942309-49f79b23-c991-4911-8aa2-bac41f238325.png">
<img width="947" alt="image" src="https://user-images.githubusercontent.com/98235097/231939381-738c7b08-d155-41a5-9a37-ac2f6751b5a0.png">
<br>
<br>
<img width="637" alt="image" src="https://user-images.githubusercontent.com/98235097/231939988-9ae9940c-90b5-468a-84b1-94bc697f3a03.png">
<br>
Uygulama'da unit test ve integration test yazilmistir
<br>
<img width="760" alt="image" src="https://user-images.githubusercontent.com/98235097/231940032-e3a5112a-f8c4-4596-8bc8-bf2cbf9b3385.png">
<img width="899" alt="image" src="https://user-images.githubusercontent.com/98235097/231938560-40c1d06c-189a-4225-b616-cb084f720897.png">
<img width="986" alt="image" src="https://user-images.githubusercontent.com/98235097/231938801-873d3994-2648-4c5c-b182-4f8b14eef16d.png">
<br>
<br>
<img width="1204" alt="image" src="https://user-images.githubusercontent.com/98235097/231942812-884996f8-a5fa-453c-bd6d-4006fb1e206c.png">
<img width="1032" alt="image" src="https://user-images.githubusercontent.com/98235097/231943044-08dd9c48-2e0d-4e1b-980a-6eeca767316f.png">
<img width="1029" alt="image" src="https://user-images.githubusercontent.com/98235097/231943831-35b88a77-1efb-4b21-a763-b1cd53907e74.png">

<img width="1713" alt="Screen Shot 2023-04-14 at 07 42 15" src="https://user-images.githubusercontent.com/52275789/231944813-34f0fc2a-74a8-4dd3-ba7e-cf9633eff981.png">
<img width="1915" alt="Screen Shot 2023-04-14 at 07 42 37" src="https://user-images.githubusercontent.com/52275789/231944815-e603e5f7-590d-4e25-8d9a-5e7b69f293e7.png">

## Bağlantılar
https://github.com/fmssbilisimtech/ecommerce-template-backend
<br>
https://github.com/gencdevops/e-commerce-microservice-backend
<br>
https://github.com/gencdevops/e-commerce-microservice-frontend