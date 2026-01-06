package br.com.torqueos.repository;

import br.com.torqueos.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	Optional<Usuario> findByIdEmpresaAndEmailAndAtivoTrue(Long idEmpresa, String email);

	Optional<Usuario> findByEmailAndAtivoTrue(String email);

	List<Usuario> findByIdEmpresa(Long idEmpresa);

	void deleteByIdUsuarioAndIdEmpresa(Long idUsuario, Long idEmpresa);

	long countByIdEmpresaAndAtivoTrue(Long idEmpresa);

	List<Usuario> findByIdEmpresaOrderByNomeAsc(Long idEmpresa);

	List<Usuario> findByIdEmpresaAndAtivoTrueOrderByNomeAsc(Long idEmpresa);

	Optional<Usuario> findByIdUsuarioAndIdEmpresa(Long idUsuario, Long idEmpresa);

}
