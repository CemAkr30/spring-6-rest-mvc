package sa.springframework.spring6restmvc.entities;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.apache.commons.lang3.builder.HashCodeExclude;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;
import sa.springframework.spring6restmvc.model.BeerStyle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
//@ToString(exclude = {"categories"})
//@JsonIdentityInfo( generator = ObjectIdGenerators.PropertyGenerator.class, property = "uuid")
public class Beer implements Serializable {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
    private UUID id;

    @Version
    private Integer version;
    @NotNull
    @NotBlank
    @Size(max = 50)
    @Column(length = 50)
    private String beerName;
    @NotNull
    private BeerStyle beerStyle;
    @NotNull
    @NotBlank
    @Size(max = 255)
    private String upc;
    private Integer quantityOnHand;

    @NotNull
    private BigDecimal price;
    @CreationTimestamp
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime updateDate;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "beer_category",
            joinColumns = @JoinColumn(name = "beer_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();


    public void addCategory(Category category){
        categories.add(category);
    }
}


/**
 * pes Ve Fetch Types
 * Merhaba arkadaşlar, bu yazımda sizlere hibernate de ilişki çeşitlerinden bahsedeceğim. Cascade nedir?
 * Çeşitleri nelerdir? Ve fetch tipleri ile ilgili Sorulara cevap vereceğim.
 *
 * Cascade nedir?
 *
 * Cascade, bir JPA standardıdır. Entity sınıflarımızdaki ilişkilerin hareketlerini yani davranışlarını
 * cascade tipleri ile ayarlarız. Yani ilişkili sınıfların birbirlerinden etkilenip etkilenmemesini sağlıyor. Örnek olarak bir değer sildiğimizde o silinen veri ilişkili olan verilerin etkilenmesini ya da etkilenmemesini sağlarız. Bu JPA standartı, veritabanımıza bulaşmadan kolaylıkla Java sınıflarımız üzerinden işlemleri yönetmemizi sağlar.
 *
 * Cascade tipleri nelerdir?
 *
 * TİP	Görevi
 * Persist	Nesne persist edilirse ilişkili nesnelerde persist edilir
 * All	Tüm işlemleri ilişkili nesnelerle birlikte yapar
 * Merge	Nesne merge edilirse ilişkili nesnelerde merge edilir
 * Remove	Nesne remove edilirse ilişkili nesnelerde remove edilir
 * Refresh	Nesne refresh edilirse ilişkili nesnelerde refresh edilir
 *
 *
 * Nullable => False değerini alırsa o sütun null olamaz(Not Null).
 *
 * Fetch Type Nedir?
 *
 * Aralarında ilişki bulunan Entity sınıflarından bir tarafın yüklenme durumunda diğer tarafın yüklenme
 * stratejisini belirlememizi sağlar. Hibernate de 2 adet fetch type vardır. Bunlar:
 *
 * 1-)Eager(Ön Yükleme)
 *
 * 2-)Lazy(Tembel/Sonradan Yükleme)
 *
 * Eğer @OneToOne ve @ManyToOne ilişkileri kullanıyorsak FetchType olarak Eager kullanmamız daha doğru olur.
 * Yani bir tane Entity nesnesi üzerinden ilişki kurulduğundan ön yükleme yapmak performans açısından sorun oluşturmaz.
 *
 * Eğer ki @OneToMany ve @ManyToMany ilişki kullanıyorsak FetchType olarak Lazy kullanmamız daha doğru olur.
 * Yani birden fazla ilişkili nesne olduğundan ön yükleme yapmamız performans a
 * çısından kayba neden olur. Bunun için ihtiyaç duyulduğunda yüklemek daha doğru olur.
 * */

/**
 * Flush, Hibernate tarafında Session, JPA'da ise EntityManager üzerinden yönetilen PersistenceContext
 * içerisinde mevcut entity'ler ile ilgili değişiklikleri (bu değişiklikler insert, update veya delete olabilir)
 * veritabanına SQL ifadeleri ile aktaran operasyondur
 * */

/**
 * A.1. Propagation.REQUIRED: .Eğer mevcutta bir transaction var ise yeni bir transaction açmadan bu transactionu kullanır,Eğer transaction
 * yoksa yeni bir transaction açar. @Transactional keywordu yazıldığında davranış şekli otomatik olarak “REQUIRED” dır.
 *
 *
 * A.2 Propagation.SUPPORTS:Eğer bir transaction var ise o transaction u kullanır .Yok ise
 * transaction’sız çalışır. Yeni bir transaction da açmaz
 *
 * A.3 Propagation.MANDATORY: Eğer bir transaction yok ise exception fırlatır
 *
 * A.4 Propagation.REQUIRES_NEW :Aktif bir transaction var ise bunu bekletir(Suspend), ve yeni bir transaction açar
 *
 *
 * A.5 Propagation..NOT_SUPPORTED: Eğer bir transaction var ise o transaction’u suspend edilir ve yen bir transaction da açmaz
 *
 * A.6 Propagation.NEVER: Eğer bir transaction var ise exception fırlatır
 *
 * A.7 Propagation.NESTED :Bu parametre JDBC teknolojisin geliştirdiği savepoint ile beraber kullanır.
 * Eğer bir transaction var ise paralelde başka bir transaction açar(Nested transaction) ve bu
 * transaction rollback olurken diğer transaction devam eder
 * .Eğer transaction yok ise “REQIRED” olarak
 * */