package com.cybersolution.imageinterface.repository;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cybersolution.imageinterface.models.User;


@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	Optional<User> findByUsername(String username);

	Boolean existsByUsername(String username);

	Boolean existsByEmail(String email);
	
	@Transactional
	@Query("Update User u set u.enabled =?2 where u.id=?1")
	@Modifying
	public void updateEnabledStatus(Integer id, boolean enabled);
}
