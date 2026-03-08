package ru.job4j.media.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_relations")
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class UserRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "from_user_id")
    private User fromUser;
    @ManyToOne
    @JoinColumn(name = "to_user_id")
    private User toUser;
    private boolean subscribed;
    private boolean friend;
}
