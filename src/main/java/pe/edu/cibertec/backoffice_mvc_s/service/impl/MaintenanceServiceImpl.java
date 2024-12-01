package pe.edu.cibertec.backoffice_mvc_s.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.cibertec.backoffice_mvc_s.dto.FilmDetailDto;
import pe.edu.cibertec.backoffice_mvc_s.dto.FilmDto;
import pe.edu.cibertec.backoffice_mvc_s.dto.FilmUpdateDto;
import pe.edu.cibertec.backoffice_mvc_s.entity.Film;
import pe.edu.cibertec.backoffice_mvc_s.entity.Language;
import pe.edu.cibertec.backoffice_mvc_s.repository.FilmRepository;
import pe.edu.cibertec.backoffice_mvc_s.service.MaintenanceService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class MaintenanceServiceImpl implements MaintenanceService {

    @Autowired
    FilmRepository filmRepository;

    @Override
    public List<FilmDto> getAllFilms() {

        List<FilmDto> films = new ArrayList<FilmDto>();
        Iterable<Film> iterable = filmRepository.findAll();
        iterable.forEach(film -> {
            FilmDto filmDto = new FilmDto(film.getFilmId(),
                    film.getTitle(),
                    film.getLanguage().getName(),
                    film.getRentalRate());
            films.add(filmDto);
        });

        return films;
    }


    @Override
    public FilmDetailDto getFilmById(int id) {

        Optional<Film> optional = filmRepository.findById(id);
        return optional.map(
                film -> new FilmDetailDto(film.getFilmId(),
                        film.getTitle(),
                        film.getDescription(),
                        film.getReleaseYear(),
                        film.getLanguage().getLanguageId(),
                        film.getLanguage().getName(),
                        film.getRentalDuration(),
                        film.getRentalRate(),
                        film.getLength(),
                        film.getReplacementCost(),
                        film.getRating(),
                        film.getSpecialFeatures(),
                        film.getLastUpdate())
        ).orElse(null);

    }

    @Override
    public FilmDetailDto updateFilmDetails(FilmUpdateDto filmUpdateDto) {
        Optional<Film> filmOptional = filmRepository.findById(filmUpdateDto.filmId());
        if (filmOptional.isPresent()) {
            Film film = filmOptional.get();

            if (filmUpdateDto.title() != null) film.setTitle(filmUpdateDto.title());
            if (filmUpdateDto.description() != null) film.setDescription(filmUpdateDto.description());
            if (filmUpdateDto.releaseYear() != null) film.setReleaseYear(filmUpdateDto.releaseYear());

            if (filmUpdateDto.languageId() != null) {
                Language language = new Language();
                language.setLanguageId(filmUpdateDto.languageId());
                film.setLanguage(language);
            }

            if (filmUpdateDto.languageName() != null && film.getLanguage() != null) {
                film.getLanguage().setName(filmUpdateDto.languageName());
            }

            if (filmUpdateDto.rentalDuration() != null) film.setRentalDuration(filmUpdateDto.rentalDuration());
            if (filmUpdateDto.rentalRate() != null) film.setRentalRate(filmUpdateDto.rentalRate());
            if (filmUpdateDto.length() != null) film.setLength(filmUpdateDto.length());
            if (filmUpdateDto.replacementCost() != null) film.setReplacementCost(filmUpdateDto.replacementCost());
            if (filmUpdateDto.rating() != null) film.setRating(filmUpdateDto.rating());
            if (filmUpdateDto.specialFeatures() != null) film.setSpecialFeatures(filmUpdateDto.specialFeatures());

            film.setLastUpdate(new java.util.Date());

            filmRepository.save(film);

            return new FilmDetailDto(film.getFilmId(), film.getTitle(), film.getDescription(), film.getReleaseYear(),
                    film.getLanguage().getLanguageId(), film.getLanguage().getName(), film.getRentalDuration(), film.getRentalRate(),
                    film.getLength(), film.getReplacementCost(), film.getRating(), film.getSpecialFeatures(), film.getLastUpdate());
        } else {
            throw new RuntimeException("No se pudo encontrar la película " + filmUpdateDto.filmId());
        }
    }

}
