package com.etnetera.hr.repository;

import com.etnetera.hr.data.JavaScriptFramework;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring data repository interface used for accessing the data in database.
 *
 * @author Etnetera
 */
public interface JavaScriptFrameworkRepository extends JpaRepository<JavaScriptFramework, Long>, JpaSpecificationExecutor<JavaScriptFramework> {


}
