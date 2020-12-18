package com.example.fuck2;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.fuck2.ui.CreateOrderCart;
import com.example.fuck2.ui.PreViewAddress;

public class CreateOrder extends AppCompatActivity {
    private LinearLayout linearLayout;
    private PreViewAddress preViewAddress;
    private CreateOrderCart createOrderCart;
    private TextView totalPriceView;

    private void addOneCart(String title, float price, int amount) {
        createOrderCart = new CreateOrderCart(CreateOrder.this);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) createOrderCart.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        layoutParams.setMargins(0, 0, 0, 10);
        createOrderCart.setLayoutParams(layoutParams);
        linearLayout.addView(createOrderCart);

    }

    void setTotalPrice(float totalPrice) {
        totalPriceView.setText(String.valueOf(totalPrice) + "¥");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println(data.getStringExtra("nick_name"));
        if (requestCode == 0 && resultCode == RESULT_OK) {
            int address_id = data.getIntExtra("address_id", 0);
            String nickName = data.getStringExtra("nick_name");
            String detail = data.getStringExtra("detail");
            String phone = data.getStringExtra("phone");
            preViewAddress.changeToSelectMode(false);
            preViewAddress.setPhone(phone);
            preViewAddress.setDetail(detail);
            preViewAddress.setNickName(nickName);
            preViewAddress.hiddenEditIcon();
            preViewAddress.setAddressId(address_id);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_order);
        linearLayout = findViewById(R.id.cart_set);
        preViewAddress = new PreViewAddress(CreateOrder.this) {
            @Override
            public void itemClick(String nick_name, String phone, String detail) {
                super.itemClick(nick_name, phone, detail);
                Intent intent = new Intent(CreateOrder.this, AddressList.class);
                startActivityForResult(intent, 0);
            }
        };
        totalPriceView = findViewById(R.id.total_price);
        preViewAddress.changeToSelectMode(true);

        linearLayout.addView(preViewAddress, 0);
        String cartId = getIntent().getStringExtra("cart_id");
        addOneCart(null, 0, 0);
        addOneCart(null, 0, 0);
        setTotalPrice(1000);
        System.out.println(cartId);
    }
}