package ua.lyashko.faceit.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class LunchEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    @JoinTable(
            name = "lunch_main_course",
            joinColumns = @JoinColumn(name = "lunch_id"),
            inverseJoinColumns = @JoinColumn(name = "main_course_id")
    )
    private List<MealEntity> mainCourse;

    @ManyToMany
    @JoinTable(
            name = "lunch_dessert",
            joinColumns = @JoinColumn(name = "lunch_id"),
            inverseJoinColumns = @JoinColumn(name = "dessert_id")
    )
    private List<MealEntity> dessert;

}
