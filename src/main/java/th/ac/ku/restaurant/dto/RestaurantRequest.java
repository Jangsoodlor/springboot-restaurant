package th.ac.ku.restaurant.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Data
public class RestaurantRequest {

    @NotBlank(message = "Name is mandatory")
    private String name;

    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating cannot exceed 5")
    private double rating;

    @NotBlank(message = "Location is mandatory")
    private String location;
}
