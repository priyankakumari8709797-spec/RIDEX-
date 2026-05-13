public class Validation {

    public static boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty();
    }

    public static boolean isValidAge(String ageStr) {
        try {
            int age = Integer.parseInt(ageStr);
            return age > 0 && age <= 100;
        } catch (Exception e) {
            return false;
        }
    }
}
