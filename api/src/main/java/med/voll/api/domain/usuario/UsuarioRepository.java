package med.voll.api.domain.usuario;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    //Metodo que faz a consulta de usuario no banco de dados pelo campo email.
    UserDetails findByLogin(String email);
}
