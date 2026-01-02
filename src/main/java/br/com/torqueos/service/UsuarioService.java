package br.com.torqueos.service;

import br.com.torqueos.auth.PasswordUtil;
import br.com.torqueos.model.Usuario;
import br.com.torqueos.repository.UsuarioRepository;
import br.com.torqueos.tenant.TenantContext;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

	private final UsuarioRepository repo;
	private final AssinaturaService assinaturaService;

	public UsuarioService(UsuarioRepository repo, AssinaturaService assinaturaService) {
		this.repo = repo;
		this.assinaturaService = assinaturaService;
	}

	public Usuario autenticar(Long empresaId, String email, String senha) {
		  return repo.findByIdEmpresaAndEmailAndAtivoTrue(empresaId, email)
		      .filter(u -> u.getSenhaHash().equals(PasswordUtil.sha256(senha)))
		      .orElseThrow(() -> new RuntimeException("Login inválido"));
		}

		public Usuario autenticarGlobal(String email, String senha) {
		  return repo.findByEmailAndAtivoTrue(email)
		      .filter(u -> "SUPERADMIN".equalsIgnoreCase(u.getRole()))
		      .filter(u -> u.getSenhaHash().equals(PasswordUtil.sha256(senha)))
		      .orElseThrow(() -> new RuntimeException("Login inválido (SUPERADMIN)"));
		}

	public List<Usuario> listar() {
		return repo.findByIdEmpresa(TenantContext.getEmpresaId());
	}

	public Usuario findById(Long id) {
		return repo.findByIdUsuarioAndIdEmpresa(id, TenantContext.getEmpresaId())
				.orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + id));
	}

	public Usuario criar(Usuario u, String senhaRaw) {
		assinaturaService.validarAssinaturaAtiva();
		// limite de usuários do plano
		long qtd = repo.countByIdEmpresaAndAtivoTrue(TenantContext.getEmpresaId());
		int limite = assinaturaService.getPlanoAtual().getLimiteUsuarios();
		if (qtd >= limite) {
			throw new RuntimeException("Limite de usuários do plano atingido (" + limite + ").");
		}

		u.setIdEmpresa(TenantContext.getEmpresaId());
		u.setSenhaHash(PasswordUtil.sha256(senhaRaw));
		if (u.getRole() == null || u.getRole().trim().isEmpty())
			u.setRole("OPERADOR");
		if (u.getAtivo() == null)
			u.setAtivo(true);
		return repo.save(u);
	}

	public Usuario update(Long id, Usuario novo, String senhaRawOpcional) {
		Usuario u = findById(id);
		u.setNome(novo.getNome());
		u.setEmail(novo.getEmail());
		u.setRole(novo.getRole());
		u.setAtivo(novo.getAtivo());
		if (senhaRawOpcional != null && !senhaRawOpcional.trim().isEmpty()) {
			u.setSenhaHash(PasswordUtil.sha256(senhaRawOpcional));
		}
		return repo.save(u);
	}

	public void delete(Long id) {
		repo.deleteByIdUsuarioAndIdEmpresa(id, TenantContext.getEmpresaId());
	}

}
