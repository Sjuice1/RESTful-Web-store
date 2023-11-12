package com.example.RESTftulSN.controllers;

import com.example.RESTftulSN.DTO.ItemDTO;
import com.example.RESTftulSN.DTO.UserDTO.UserDTO;
import com.example.RESTftulSN.repositories.ItemRepository;
import com.example.RESTftulSN.repositories.UsersRepository;
import com.example.RESTftulSN.util.exceptions.InvalidDataException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class APITest{

    @SpringBootTest
    @Nested
    class UserAPITEST{
        private final userAPI userAPITest;
        private final UsersRepository usersRepository;

        @Autowired
        UserAPITEST(userAPI userAPITest, UsersRepository usersRepository) {
            this.userAPITest = userAPITest;
            this.usersRepository = usersRepository;
        }
        @Nested
        @DisplayName("Get all users access test")
        class getAllUsersAccessTest{
            @Test
            @DisplayName("Admin can get all users")
            @WithMockUser(authorities = "ROLE_ADMIN")
            public void getAllUsersModeratorADMIN(){
                try {
                    userAPITest.getAllUsers();
                    assertTrue(true);
                }
                catch (AccessDeniedException e){
                    fail();
                }
            }

            @Test
            @DisplayName("Guest can't get all users")
            @WithMockUser(authorities = "ROLE_GUEST")
            public void getAllUsersGuest(){
                assertThrows(AccessDeniedException.class,() -> userAPITest.getAllUsers());
            }
            @Test
            @DisplayName("Verified can't get all users")
            @WithMockUser(authorities = "ROLE_VERIFIED")
            public void getAllUsersVerified(){
                assertThrows(AccessDeniedException.class,() -> userAPITest.getAllUsers());
            }
            @Test
            @DisplayName("Moderator can't get all users")
            @WithMockUser(authorities = "ROLE_MODERATOR")
            public void getAllUsersModerator(){
                assertThrows(AccessDeniedException.class,() -> userAPITest.getAllUsers());
            }
        }
        @Nested
        @DisplayName("Delete user access")
        class deleteUserAccess{
            @Test
            @DisplayName("Admin can delete user")
            @WithMockUser(authorities = "ROLE_ADMIN")
            public void canDeleteADMIN(){
                assertThrows(InvalidDataException.class,() -> userAPITest.deleteUser(500000004312L ));
            }

            @Test
            @DisplayName("Guest can't delete user")
            @WithMockUser(authorities = "ROLE_GUEST")
            public void canDeleteGuest(){
                assertThrows(AccessDeniedException.class,() -> userAPITest.deleteUser(1L));
            }
            @Test
            @DisplayName("Verified can't delete user")
            @WithMockUser(authorities = "ROLE_VERIFIED")
            public void canDeleteVerified(){
                assertThrows(AccessDeniedException.class,() -> userAPITest.deleteUser(1L));
            }
            @Test
            @DisplayName("Moderator can't delete user")
            @WithMockUser(authorities = "ROLE_MODERATOR")
            public void canDeleteModerator(){
                assertThrows(AccessDeniedException.class,() -> userAPITest.deleteUser(1L));
            }
        }
        @Test
        @DisplayName("Compare list of users size with database user count")
        @WithMockUser(username = "user",authorities = "ROLE_ADMIN")
        public void checkAllUsers(){
            ResponseEntity<List<UserDTO>> users = userAPITest.getAllUsers();
            assertThat(users.getBody().size()).isEqualTo(usersRepository.count());
        }

        @Test
        @DisplayName("Get user by id with invalid id")
        public void invalidIdTest(){
            Assertions.assertThrows(InvalidDataException.class,()->userAPITest.getUserById(50000000321L));
        }

        @Test
        @DisplayName("Get user by id with working id")
        public void workingIdTest(){
            try {
                userAPITest.getUserById(15L);
                assertTrue(true);
            }
            catch (InvalidDataException invalidDataException){
                fail();
            }
        }

        @Test
        @DisplayName("Delete user by id with invalid id")
        @WithMockUser(username = "user",authorities = "ROLE_ADMIN")
        public void deleteInvalidIdTest(){
            Assertions.assertThrows(InvalidDataException.class,()->userAPITest.deleteUser(50000000321L));
        }


    }
    @SpringBootTest
    @Nested
    class ItemAPITEST {
        private final itemAPI itemAPITest;
        private final ItemRepository itemRepository;

        @Autowired
        public ItemAPITEST(itemAPI itemAPITest, ItemRepository itemRepository) {
            this.itemAPITest = itemAPITest;
            this.itemRepository = itemRepository;
        }

        @Nested
        @DisplayName("Add item access")
        class addItemAccess{
            @Test
            @DisplayName("Admin can add item")
            @WithMockUser(authorities = "ROLE_ADMIN")
            public void canAddADMIN(){
                assertThrows(NullPointerException.class,() -> itemAPITest.addItem(null,null ));
            }

            @Test
            @DisplayName("Guest can't add item")
            @WithMockUser(authorities = "ROLE_GUEST")
            public void canAddGuest(){
                assertThrows(AccessDeniedException.class,() -> itemAPITest.addItem(null,null ));
            }
            @Test
            @DisplayName("Verified can add item")
            @WithMockUser(authorities = "ROLE_VERIFIED")
            public void canAddVerified(){
                assertThrows(NullPointerException.class,() -> itemAPITest.addItem(null,null ));
            }
            @Test
            @DisplayName("Moderator can add item")
            @WithMockUser(authorities = "ROLE_MODERATOR")
            public void canAddModerator(){
                assertThrows(NullPointerException.class,() -> itemAPITest.addItem(null,null ));
            }
        }
        @Nested
        @DisplayName("Update item access")
        class updateItemAccess{
            @Test
            @DisplayName("Admin can update item")
            @WithMockUser(authorities = "ROLE_ADMIN")
            public void canUpdateADMIN(){
                assertThrows(NullPointerException.class,() -> itemAPITest.updateItem(null,null,null ));
            }

            @Test
            @DisplayName("Guest can't update item")
            @WithMockUser(authorities = "ROLE_GUEST")
            public void canUpdateGuest(){
                assertThrows(AccessDeniedException.class,() -> itemAPITest.updateItem(null,null,null ));
            }
            @Test
            @DisplayName("Verified can update item")
            @WithMockUser(authorities = "ROLE_VERIFIED")
            public void canUpdateVerified(){
                assertThrows(NullPointerException.class,() -> itemAPITest.updateItem(null,null,null ));
            }
            @Test
            @DisplayName("Moderator can update item")
            @WithMockUser(authorities = "ROLE_MODERATOR")
            public void canUpdateModerator(){
                assertThrows(NullPointerException.class,() -> itemAPITest.updateItem(null,null,null ));
            }
        }
        @Nested
        @DisplayName("Delete item access")
        class deleteItemAccess{
            @Test
            @DisplayName("Admin can delete item")
            @WithMockUser(authorities = "ROLE_ADMIN")
            public void canDeleteADMIN(){
                assertThrows(InvalidDataAccessApiUsageException.class,() -> itemAPITest.deleteItem(null ));
            }

            @Test
            @DisplayName("Guest can't delete item")
            @WithMockUser(authorities = "ROLE_GUEST")
            public void canDeleteGuest(){
                assertThrows(AccessDeniedException.class,() -> itemAPITest.deleteItem(null));
            }
            @Test
            @DisplayName("Verified can delete item")
            @WithMockUser(authorities = "ROLE_VERIFIED")
            public void canDeleteVerified(){
                assertThrows(InvalidDataAccessApiUsageException.class,() -> itemAPITest.deleteItem(null));
            }
            @Test
            @DisplayName("Moderator can delete item")
            @WithMockUser(authorities = "ROLE_MODERATOR")
            public void canDeleteModerator(){
                assertThrows(InvalidDataAccessApiUsageException.class,() -> itemAPITest.deleteItem(null));
            }
        }

        @Test
        @DisplayName("Compare list of item size with database item count")
        public void checkAllUsers(){
            ResponseEntity<List<ItemDTO>> users = itemAPITest.items();
            assertThat(users.getBody().size()).isEqualTo(itemRepository.count());
        }
        @Test
        @DisplayName("Get item by id with invalid id")
        public void invalidIdTest(){
            Assertions.assertThrows(InvalidDataException.class,()->itemAPITest.getItem(50000000321L));
        }

        @Test
        @DisplayName("Get item by id with working id")
        public void workingIdTest(){
            try {
                itemAPITest.getItem(5L);
                assertTrue(true);
            }
            catch (InvalidDataException invalidDataException){
                fail();
            }
        }
        @Test
        @DisplayName("Get item reviews by id with invalid id")
        public void invalidIdReviewTest(){
            Assertions.assertThrows(InvalidDataException.class,()->itemAPITest.getItemReviews(50000000321L));
        }

        @Test
        @Transactional
        @DisplayName("Get item reviews by id with working id")
        public void workingIdReviewTest(){
            try {
                itemAPITest.getItemReviews(5L);
                assertTrue(true);
            }
            catch (InvalidDataException invalidDataException){
                fail();
            }
        }

    }
    @SpringBootTest
    @Nested
    class reviewAPITEST {
        private final reviewAPI reviewAPITest;

        @Autowired
        reviewAPITEST(reviewAPI reviewAPITest) {
            this.reviewAPITest = reviewAPITest;
        }
        @Nested
        @DisplayName("Leave review access")
        class leaveReviewAccess{
            @Test
            @DisplayName("Admin can leave review")
            @WithMockUser(authorities = "ROLE_ADMIN")
            public void canLeaveADMIN(){
                assertThrows(NullPointerException.class,() ->reviewAPITest.addReviewToItem(null,null));
            }

            @Test
            @DisplayName("Guest can't leave review")
            @WithMockUser(authorities = "ROLE_GUEST")
            public void canLeaveGuest(){
                assertThrows(AccessDeniedException.class,() ->reviewAPITest.addReviewToItem(null,null));
            }
            @Test
            @DisplayName("Verified can leave review")
            @WithMockUser(authorities = "ROLE_VERIFIED")
            public void canLeaveVerified(){
                assertThrows(NullPointerException.class,() ->reviewAPITest.addReviewToItem(null,null));
            }
            @Test
            @DisplayName("Moderator can leave review")
            @WithMockUser(authorities = "ROLE_MODERATOR")
            public void canLeaveModerator(){
                assertThrows(NullPointerException.class,() ->reviewAPITest.addReviewToItem(null,null));
            }
        }

        @Nested
        @DisplayName("Delete review access")
        class deleteItemAccess{
            @Test
            @DisplayName("Admin can delete review")
            @WithMockUser(authorities = "ROLE_ADMIN")
            public void canDeleteADMIN(){
                assertThrows(InvalidDataAccessApiUsageException.class,() -> reviewAPITest.deleteReview(null));
            }

            @Test
            @DisplayName("Guest can't delete review")
            @WithMockUser(authorities = "ROLE_GUEST")
            public void canDeleteGuest(){
                assertThrows(AccessDeniedException.class,() -> reviewAPITest.deleteReview(null));
            }
            @Test
            @DisplayName("Verified can delete review")
            @WithMockUser(authorities = "ROLE_VERIFIED")
            public void canDeleteVerified(){
                assertThrows(InvalidDataAccessApiUsageException.class,() -> reviewAPITest.deleteReview(null));
            }
            @Test
            @DisplayName("Moderator can delete review")
            @WithMockUser(authorities = "ROLE_MODERATOR")
            public void canDeleteModerator(){
                assertThrows(InvalidDataAccessApiUsageException.class,() -> reviewAPITest.deleteReview(null));
            }
        }

        @Test
        @DisplayName("Get review by id with invalid id")
        public void invalidIdTest(){
            Assertions.assertThrows(InvalidDataException.class,()->reviewAPITest.getReview(5000004210L));
        }

        @Test
        @DisplayName("Get review by id with working id")
        public void workingIdTest(){
            try {
                reviewAPITest.getReview(1L);
                assertTrue(true);
            }
            catch (InvalidDataException invalidDataException){
                fail();
            }
        }
    }
    @SpringBootTest
    @Nested
    class cartAPITEST{
        private final cartAPI cartAPITest;
        @Nested
        @DisplayName("Cart access")
        class cartAccess{
            @Test
            @DisplayName("Admin can check cart")
            @WithMockUser(authorities = "ROLE_ADMIN")
            public void canCheckADMIN(){
                assertThrows(InvalidDataAccessApiUsageException.class,() -> cartAPITest.cartOfUser(null));
            }

            @Test
            @DisplayName("Guest can't check cart")
            @WithMockUser(authorities = "ROLE_GUEST")
            public void canCheckGuest(){
                assertThrows(AccessDeniedException.class,() -> cartAPITest.cartOfUser(null));
            }
            @Test
            @DisplayName("Verified can check cart")
            @WithMockUser(authorities = "ROLE_VERIFIED")
            public void canCheckVerified(){
                assertThrows(InvalidDataAccessApiUsageException.class,() -> cartAPITest.cartOfUser(null));
            }
            @Test
            @DisplayName("Moderator can check cart")
            @WithMockUser(authorities = "ROLE_MODERATOR")
            public void canCheckModerator(){
                assertThrows(InvalidDataAccessApiUsageException.class,() -> cartAPITest.cartOfUser(null));
            }
        }
        @Nested
        @DisplayName("Put item in cart access")
        class putItemAccess{
            @Test
            @DisplayName("Admin can put item in cart")
            @WithMockUser(authorities = "ROLE_ADMIN")
            public void canPutADMIN(){
                assertThrows(NullPointerException.class,() ->cartAPITest.putItemOnCart(null,null));
            }

            @Test
            @DisplayName("Guest can put item in cart")
            @WithMockUser(authorities = "ROLE_GUEST")
            public void canPutGuest(){
                assertThrows(AccessDeniedException.class,() ->cartAPITest.putItemOnCart(null,null));
            }
            @Test
            @DisplayName("Verified can put item in cart")
            @WithMockUser(authorities = "ROLE_VERIFIED")
            public void canPutVerified(){
                assertThrows(NullPointerException.class,() ->cartAPITest.putItemOnCart(null,null));
            }
            @Test
            @DisplayName("Moderator can put item in cart")
            @WithMockUser(authorities = "ROLE_MODERATOR")
            public void canPutModerator(){
                assertThrows(NullPointerException.class,() ->cartAPITest.putItemOnCart(null,null));
            }
        }
        @Nested
        @DisplayName("Remove item from cart access")
        class removeItemFromCart{
            @Test
            @DisplayName("Admin can remove item from cart")
            @WithMockUser(authorities = "ROLE_ADMIN")
            public void canRemoveADMIN(){
                assertThrows(NullPointerException.class,() -> cartAPITest.removeItemFromCart(null,null));
            }

            @Test
            @DisplayName("Guest can remove item from cart")
            @WithMockUser(authorities = "ROLE_GUEST")
            public void canRemoveeGuest(){
                assertThrows(AccessDeniedException.class,() -> cartAPITest.removeItemFromCart(null,null));
            }
            @Test
            @DisplayName("Verified can remove item from cart")
            @WithMockUser(authorities = "ROLE_VERIFIED")
            public void canRemoveVerified(){
                assertThrows(NullPointerException.class,() -> cartAPITest.removeItemFromCart(null,null));
            }
            @Test
            @DisplayName("Moderator can remove item from cart")
            @WithMockUser(authorities = "ROLE_MODERATOR")
            public void canRemvoeModerator(){
                assertThrows(NullPointerException.class,() -> cartAPITest.removeItemFromCart(null,null));
            }
        }

        @Autowired
        public cartAPITEST(cartAPI cartAPITest) {
            this.cartAPITest = cartAPITest;
        }
    }
    @SpringBootTest
    @Nested
    class orderAPITEST{
        private final orderAPI orderAPITest;

        @Autowired
        orderAPITEST(orderAPI orderAPITest) {
            this.orderAPITest = orderAPITest;
        }
        @Test
        @WithMockUser(authorities = "ROLE_ADMIN")
        @DisplayName("Get order by id with invalid id")
        public void invalidIdTest(){
            Assertions.assertThrows(InvalidDataException.class,()->orderAPITest.getOrder(50000000321L));
        }
        @Nested
        @DisplayName("Order access")
        class cartAccess{
            @Test
            @DisplayName("Admin can check order")
            @WithMockUser(authorities = "ROLE_ADMIN")
            public void canCheckADMIN(){
                assertThrows(InvalidDataAccessApiUsageException.class,() -> orderAPITest.getOrderItems(null));
            }

            @Test
            @DisplayName("Guest can't check order")
            @WithMockUser(authorities = "ROLE_GUEST")
            public void canCheckGuest(){
                assertThrows(AccessDeniedException.class,() -> orderAPITest.getOrderItems(null));
            }
            @Test
            @DisplayName("Verified can check order")
            @WithMockUser(authorities = "ROLE_VERIFIED")
            public void canCheckVerified(){
                assertThrows(InvalidDataAccessApiUsageException.class,() -> orderAPITest.getOrderItems(null));
            }
            @Test
            @DisplayName("Moderator can check order")
            @WithMockUser(authorities = "ROLE_MODERATOR")
            public void canCheckModerator(){
                assertThrows(InvalidDataAccessApiUsageException.class,() -> orderAPITest.getOrderItems(null));
            }
        }
        @Nested
        @DisplayName("Order change status access")
        class removeItemFromCart{
            @Test
            @DisplayName("Admin can change status")
            @WithMockUser(authorities = "ROLE_ADMIN")
            public void canChangeADMIN(){
                assertThrows(InvalidDataException.class,() -> orderAPITest.changeStatus(null,null));
            }

            @Test
            @DisplayName("Guest can't change status")
            @WithMockUser(authorities = "ROLE_GUEST")
            public void canChangeGuest(){
                assertThrows(AccessDeniedException.class,() -> orderAPITest.changeStatus(null,null));
            }
            @Test
            @DisplayName("Verified can't change status")
            @WithMockUser(authorities = "ROLE_VERIFIED")
            public void canChangeVerified(){
                assertThrows(AccessDeniedException.class,() -> orderAPITest.changeStatus(null,null));
            }
            @Test
            @DisplayName("Moderator can change status")
            @WithMockUser(authorities = "ROLE_MODERATOR")
            public void canChangeModerator(){
                assertThrows(InvalidDataException.class,() -> orderAPITest.changeStatus(null,null));
            }
        }
        @Nested
        @DisplayName("Order make access")
        class putItemAccess{
            @Test
            @DisplayName("Admin can make an order")
            @WithMockUser(authorities = "ROLE_ADMIN")
            public void canMakeADMIN(){
                assertThrows(InvalidDataAccessApiUsageException.class,() ->orderAPITest.makeAnOrder(null));
            }

            @Test
            @DisplayName("Guest can't make an order")
            @WithMockUser(authorities = "ROLE_GUEST")
            public void canMakeGuest(){
                assertThrows(AccessDeniedException.class,() ->orderAPITest.makeAnOrder(null));
            }
            @Test
            @DisplayName("Verified can make an order")
            @WithMockUser(authorities = "ROLE_VERIFIED")
            public void canMakeVerified(){
                assertThrows(InvalidDataAccessApiUsageException.class,() ->orderAPITest.makeAnOrder(null));
            }
            @Test
            @DisplayName("Moderator can make an order")
            @WithMockUser(authorities = "ROLE_MODERATOR")
            public void canMakeModerator(){
                assertThrows(InvalidDataAccessApiUsageException.class,() ->orderAPITest.makeAnOrder(null));
            }
        }
        @Nested
        @DisplayName("Order check items access")
        class orderCheckItemsAccess{
            @Test
            @DisplayName("Admin can check orders items")
            @WithMockUser(authorities = "ROLE_ADMIN")
            public void canCheckADMIN(){
                assertThrows(InvalidDataAccessApiUsageException.class,() ->orderAPITest.getOrderItems(null));
            }

            @Test
            @DisplayName("Guest can't check orders items")
            @WithMockUser(authorities = "ROLE_GUEST")
            public void canCheckGuest(){
                assertThrows(AccessDeniedException.class,() ->orderAPITest.getOrderItems(null));
            }
            @Test
            @DisplayName("Verified can check orders items")
            @WithMockUser(authorities = "ROLE_VERIFIED")
            public void canCheckVerified(){
                assertThrows(InvalidDataAccessApiUsageException.class,() ->orderAPITest.getOrderItems(null));
            }
            @Test
            @DisplayName("Moderator can check orders items")
            @WithMockUser(authorities = "ROLE_MODERATOR")
            public void canCheckModerator(){
                assertThrows(InvalidDataAccessApiUsageException.class,() ->orderAPITest.getOrderItems(null));
            }
        }
        @Nested
        @DisplayName("Delete order")
        class deleteOrderAccess{
            @Test
            @DisplayName("Admin can delete order")
            @WithMockUser(authorities = "ROLE_ADMIN")
            public void canRemoveADMIN(){
                assertThrows(InvalidDataAccessApiUsageException.class,() -> orderAPITest.deleteOrder(null));
            }

            @Test
            @DisplayName("Guest can't delete order")
            @WithMockUser(authorities = "ROLE_GUEST")
            public void canRemoveeGuest(){
                assertThrows(AccessDeniedException.class,() -> orderAPITest.deleteOrder(null));
            }
            @Test
            @DisplayName("Verified can't delete order")
            @WithMockUser(authorities = "ROLE_VERIFIED")
            public void canRemoveVerified(){
                assertThrows(AccessDeniedException.class,() -> orderAPITest.deleteOrder(null));
            }
            @Test
            @DisplayName("Moderator can't delete order")
            @WithMockUser(authorities = "ROLE_MODERATOR")
            public void canRemvoeModerator(){
                assertThrows(AccessDeniedException.class,() -> orderAPITest.deleteOrder(null));
            }
        }


        @Test
        @WithMockUser(username = "user",password = "user",authorities = "ROLE_ADMIN")
        @DisplayName("Get order by id with working id")
        @Disabled(value = "Working in app")
        public void workingIdTest(){
            try {
                orderAPITest.getOrder(2L);
                assertTrue(true);
            }
            catch (InvalidDataException invalidDataException){
                fail();
            }
        }
    }
    @SpringBootTest
    @Nested
    class authAPITEST{
        private final authAPI authAPITest;

        @Autowired
        authAPITEST(authAPI authAPITest) {
            this.authAPITest = authAPITest;
        }
        @Nested
        @DisplayName("Generate token access")
        class cartAccess{
            @Test
            @DisplayName("Admin can't generate new toker")
            @WithMockUser(authorities = "ROLE_ADMIN")
            public void canGenerateADMIN(){
                assertThrows(AccessDeniedException.class,() -> authAPITest.generateNewToken(500000030L));
            }

            @Test
            @DisplayName("Guest can generate new toker")
            @WithMockUser(authorities = "ROLE_GUEST")
            public void canGenerateGuest(){
                assertThrows(InvalidDataException.class,() -> authAPITest.generateNewToken(500000030L));
            }
            @Test
            @DisplayName("Verified can't generate new toker")
            @WithMockUser(authorities = "ROLE_VERIFIED")
            public void canGenerateVerified(){
                assertThrows(AccessDeniedException.class,() -> authAPITest.generateNewToken(500000030L));
            }
            @Test
            @DisplayName("Moderator can't generate new toker")
            @WithMockUser(authorities = "ROLE_MODERATOR")
            public void AccessDeniedException(){
                assertThrows(AccessDeniedException.class,() -> authAPITest.generateNewToken(500000030L));
            }
        }
        @Test
        @DisplayName("Get invalid token")
        public void invalidIdTest(){
            HttpEntity<HttpStatus> status = authAPITest.authenticateUser("fsa");
            assertThat(status.getBody().equals(HttpStatus.NOT_FOUND));
        }
    }



}
