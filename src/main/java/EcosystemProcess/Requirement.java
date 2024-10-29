package EcosystemProcess;

import java.util.Objects;

public record Requirement(String requirement, int minRequirementValue, int maxRequirementValue) {

    public boolean isWithinRange(int value) {
        return value < minRequirementValue || value > maxRequirementValue;
    }

    @Override
    public String requirement() {
        return requirement;
    }

    @Override
    public boolean equals(Object o) {
        boolean result;
        if (this == o) {
            result = true;
        } else if (!(o instanceof Requirement that)) {
            result = false;
        } else {
            result = minRequirementValue == that.minRequirementValue &&
                    maxRequirementValue == that.maxRequirementValue &&
                    Objects.equals(requirement, that.requirement);
        }
        return result;
    }

    @Override
    public int hashCode() {
        return Objects.hash(requirement, minRequirementValue, maxRequirementValue);
    }

    @Override
    public String toString() {
        return "Requirement{" +
                "requirement='" + requirement + '\'' +
                ", minRequirementValue=" + minRequirementValue +
                ", maxRequirementValue=" + maxRequirementValue +
                '}';
    }

    @Override
    public int minRequirementValue() {
        return minRequirementValue;
    }

    @Override
    public int maxRequirementValue() {
        return maxRequirementValue;
    }

}
